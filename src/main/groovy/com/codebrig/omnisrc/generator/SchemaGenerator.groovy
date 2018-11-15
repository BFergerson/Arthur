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
import org.bblfsh.client.BblfshClient
import org.eclipse.jgit.api.Git
import org.kohsuke.github.GHDirection
import org.kohsuke.github.GHRepositorySearchBuilder
import org.kohsuke.github.GitHub

import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

import static com.google.common.io.Files.getFileExtension

/**
 * Used to observe a source code language and generate the resulting schema
 *
 * @version 0.2
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class SchemaGenerator {

    private static final ExecutorService THREAD_POOL = Executors.newWorkStealingPool()
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
        return observeLanguage(language, Integer.MAX_VALUE)
    }

    ObservedLanguage observeLanguage(SourceLanguage language, int parseProjectCount) {
        return observeLanguage(language, parseProjectCount, Integer.MAX_VALUE)
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
        parseGithubRepository(repoName, observedLanguage, Integer.MAX_VALUE)
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
        parseLocalRepo(localRoot, observedLanguage, Integer.MAX_VALUE)
    }

    private void parseLocalRepo(File localRoot, ObservedLanguage observedLanguage, int parseFileLimit) {
        def failCount = new AtomicInteger(0)
        def parseCount = new AtomicInteger(0)
        def sourceFiles = new ArrayList<File>()

        localRoot.eachFileRecurse(FileType.FILES) { file ->
            if (observedLanguage.language.isValidExtension(getFileExtension(file.name))) {
                sourceFiles.add(file)
                if (!file.exists()) {
                    System.err.println("Skipping non-existent file: " + file)
                    return
                } else if (parseCount.get() >= parseFileLimit) {
                    return
                }

                println "Parsing: " + file
                try {
                    def resp = timedCall(new Callable<ParseResponse>() {
                        ParseResponse call() throws Exception {
                            return client.parse(file.name, file.text, observedLanguage.language.key, Encoding.UTF8$.MODULE$)
                        }
                    }, 15, TimeUnit.SECONDS)
                    if (resp == null) {
                        System.err.println "Got null parse response"
                        //todo: understand this
                    } else if (resp.status().isOk()) {
                        def rootSourceNode = new SourceNode(observedLanguage.language, resp.uast)
                        if (filter.evaluate(rootSourceNode)) {
                            observeSourceNode(observedLanguage, rootSourceNode)
                        }
                        extractSchema(observedLanguage, rootSourceNode)
                        parseCount.getAndIncrement()
                    } else {
                        System.err.println("Failed to parse: " + file + " - Reason: " + resp.errors().toList().toString())
                        failCount.getAndIncrement()
                    }
                } catch (TimeoutException e) {
                    System.err.println("Timed out parsing: " + file)
                    failCount.getAndIncrement()
                }
            }
        }

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

    private static <T> T timedCall(Callable<T> c, long timeout, TimeUnit timeUnit)
            throws InterruptedException, ExecutionException, TimeoutException {
        FutureTask<T> task = new FutureTask<T>(c)
        THREAD_POOL.execute(task)
        return task.get(timeout, timeUnit)
    }
}
