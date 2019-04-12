package com.codebrig.omnisrc.generator

import com.codebrig.omnisrc.SourceLanguage
import com.codebrig.omnisrc.SourceNode
import com.codebrig.omnisrc.SourceNodeFilter
import com.codebrig.omnisrc.observe.ObservationConfig
import com.codebrig.omnisrc.observe.ObservedLanguage
import com.codebrig.omnisrc.observe.filter.WildcardFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import gopkg.in.bblfsh.sdk.v1.protocol.generated.ParseResponse
import groovy.io.FileType
import groovy.transform.Canonical
import groovy.transform.TupleConstructor
import org.bblfsh.client.BblfshClient
import org.eclipse.jgit.api.Git
import org.kohsuke.github.GHDirection
import org.kohsuke.github.GHRepositorySearchBuilder
import org.kohsuke.github.GitHub

import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

import static com.google.common.io.Files.getFileExtension

/**
 * Used to observe a source code language and generate the resulting schema
 *
 * @version 0.3
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class SchemaGenerator {

    private static final int MAX_PARSE_WAIT_SECONDS = 15
    private static final int MAX_FILE_PARSE_COUNT = Integer.MAX_VALUE
    private final BblfshClient client
    private SourceNodeFilter filter
    private final ObservationConfig config

    SchemaGenerator() {
        this(new BblfshClient("0.0.0.0", 9432, Integer.MAX_VALUE))
    }

    SchemaGenerator(ObservationConfig observationConfig) {
        this(new BblfshClient("0.0.0.0", 9432, Integer.MAX_VALUE), observationConfig)
    }

    SchemaGenerator(BblfshClient client) {
        this(client, ObservationConfig.baseStructure())
    }

    SchemaGenerator(BblfshClient client, ObservationConfig observationConfig) {
        this.client = client
        this.filter = new WildcardFilter()
        this.config = observationConfig
    }

    void setFilter(SourceNodeFilter filter) {
        this.filter = Objects.requireNonNull(filter)
    }

    ObservedLanguage observeLanguage(SourceLanguage language) {
        return observeLanguage(language, MAX_FILE_PARSE_COUNT)
    }

    ObservedLanguage observeLanguage(SourceLanguage language, int parseProjectCount) {
        return observeLanguage(language, parseProjectCount, MAX_FILE_PARSE_COUNT)
    }

    ObservedLanguage observeLanguage(SourceLanguage language, int parseProjectCount, int parseFilesPerProject) {
        def observedLanguage = new ObservedLanguage(language, config)
        int parsedProjects = 0
        GitHub.connectAnonymously().searchRepositories()
                .sort(GHRepositorySearchBuilder.Sort.STARS)
                .order(GHDirection.DESC)
                .language(language.key)
                .list().find {
            if (parsedProjects++ >= parseProjectCount) return true

            parseGithubRepository(it.fullName, observedLanguage, parseFilesPerProject)
            return false
        }
        return observedLanguage
    }

    ObservedLanguage observeLanguage(SourceLanguage language, File inputDirectory) {
        def observedLanguage = new ObservedLanguage(language, config)
        parseLocalRepo(inputDirectory, observedLanguage)
        return observedLanguage
    }

    void parseGithubRepository(String repoName, ObservedLanguage observedLanguage) {
        parseGithubRepository(repoName, observedLanguage, MAX_FILE_PARSE_COUNT)
    }

    void parseGithubRepository(String repoName, ObservedLanguage observedLanguage, int parseFileLimit) {
        def outputFolder = new File("/tmp/omnisrc/out/$repoName")
        if (new File(outputFolder, "cloned.omnisrc").exists()) {
            println "$repoName already exists. Repository cloning skipped."
        } else {
            if (outputFolder.exists()) outputFolder.deleteDir()
            outputFolder.mkdirs()
            cloneRepo(repoName, outputFolder)
        }
        parseLocalRepo(outputFolder, observedLanguage, parseFileLimit)
    }

    private void parseLocalRepo(File localRoot, ObservedLanguage observedLanguage) {
        parseLocalRepo(localRoot, observedLanguage, MAX_FILE_PARSE_COUNT)
    }

    private void parseLocalRepo(File localRoot, ObservedLanguage observedLanguage, int parseFileLimit) {
        def sourceFiles = new ArrayList<File>()
        localRoot.eachFileRecurse(FileType.FILES) { file ->
            if (observedLanguage.language.isValidExtension(getFileExtension(file.name))) {
                if (file.exists()) {
                    sourceFiles.add(file)
                } else {
                    System.err.println("Skipping non-existent file: " + file)
                }
            }
        }

        def failCount = new AtomicInteger(0)
        def parseCount = new AtomicInteger(0)
        def executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
        sourceFiles.parallelStream().map({ file ->
            if (parseCount.get() >= parseFileLimit) {
                return null
            }

            println "Parsing: " + file
            def fileResponse = new FileParseResponse(file)
            def task = executorService.submit({
                fileResponse.parseResponse = client.parse(file.name, file.text, observedLanguage.language.key, Encoding.UTF8$.MODULE$)
                return fileResponse
            } as Callable<FileParseResponse>)
            try {
                return task.get(MAX_PARSE_WAIT_SECONDS, TimeUnit.SECONDS)
            } catch (Exception e) {
                System.err.println("Failed to parse: " + file + " - Reason: " + e.message)
                return null
            }
        }).map({
            if (it instanceof FileParseResponse) {
                if (it.parseResponse.status().isOk()) {
                    def rootSourceNode = new SourceNode(observedLanguage.language, it.parseResponse.uast)
                    if (filter.evaluate(rootSourceNode)) {
                        observeSourceNode(observedLanguage, rootSourceNode)
                    }
                    extractSchema(observedLanguage, rootSourceNode)
                    parseCount.getAndIncrement()
                } else {
                    System.err.println("Failed to parse: " + it.parsedFile + " - Reason: " + it.parseResponse.errors().toList().toString())
                    failCount.getAndIncrement()
                }
            }
        })
        executorService.shutdown()

        println "Parsed " + parseCount.get() + " " + observedLanguage.language.qualifiedName + " files"
        if (failCount.get() > 0) {
            System.err.println("Failed to parse " + failCount.get() + " " + observedLanguage.language.qualifiedName + " files")
        }
    }

    private void extractSchema(ObservedLanguage observedLanguage, SourceNode rootSourceNode) {
        Stack<SourceNode> parentStack = new Stack<>()
        Stack<Iterator<SourceNode>> childrenStack = new Stack<>()
        parentStack.push(rootSourceNode)
        childrenStack.push(rootSourceNode.children)

        while (!parentStack.isEmpty() && !childrenStack.isEmpty()) {
            def parent = parentStack.pop()
            def children = childrenStack.pop()

            children.each {
                if (filter.evaluate(it)) {
                    parentStack.push(it)
                    childrenStack.push(it.children)
                    observeSourceNode(observedLanguage, it)

                    //parent and child don't relate in any way besides parent/child
                    if (!parent.underlyingNode.children().contains(it.underlyingNode)) {
                        observedLanguage.observeParentChildRelation(parent.internalType, it)
                    }
                } else {
                    parentStack.push(parent)
                    childrenStack.push(it.children)
                }
            }
        }
    }

    private void observeSourceNode(ObservedLanguage observedLanguage, SourceNode sourceNode) {
        if (sourceNode.internalType.isEmpty()) {
            return //todo: understand this
        }

        observedLanguage.observeAttributes(sourceNode.internalType, sourceNode.properties)
        observedLanguage.observeRelations(sourceNode.internalType, filter.getFilteredNodes(sourceNode.children))
        observedLanguage.observeRoles(sourceNode.internalType, sourceNode.roles)
        if (sourceNode.isLiteralNode()) {
            def literalMap = new HashMap<String, String>()
            sourceNode.getPossibleLiteralAttributes().each {
                literalMap.put(it, null)
            }
            observedLanguage.observeAttributes(sourceNode.internalType, literalMap)
        }
    }

    static void cloneRepo(String githubRepository, File outputDirectory) {
        println "Cloning: $githubRepository"
        Git.cloneRepository()
                .setURI("https://github.com/" + githubRepository + ".git")
                .setDirectory(outputDirectory)
                .setCloneSubmodules(true)
                .setTimeout(TimeUnit.MINUTES.toSeconds(5) as int)
                .call()
        new File(outputDirectory, "cloned.omnisrc").createNewFile()
        println "Cloned: $githubRepository"
    }

    @Canonical
    @TupleConstructor
    private static class FileParseResponse {
        File parsedFile
        ParseResponse parseResponse
    }
}
