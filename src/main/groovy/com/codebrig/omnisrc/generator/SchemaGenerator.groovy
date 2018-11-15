package com.codebrig.omnisrc.generator

import com.codebrig.omnisrc.SourceLanguage
import com.codebrig.omnisrc.SourceNode
import com.codebrig.omnisrc.SourceNodeFilter
import com.codebrig.omnisrc.observe.ObservationConfig
import com.codebrig.omnisrc.observe.ObservedLanguage
import com.codebrig.omnisrc.observe.filter.WildcardFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import groovy.io.FileType
import groovyx.gpars.GParsPool
import org.bblfsh.client.BblfshClient
import org.eclipse.jgit.api.Git
import org.kohsuke.github.GHDirection
import org.kohsuke.github.GHRepositorySearchBuilder
import org.kohsuke.github.GitHub

import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import java.util.stream.Collectors

import static com.google.common.io.Files.getFileExtension

/**
 * Used to observe a source code language and generate the resulting schema
 *
 * @version 0.2
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class SchemaGenerator {

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
        def sourceFiles = new ArrayList<File>()
        localRoot.eachFileRecurse(FileType.FILES) {
            if (observedLanguage.language.isValidExtension(getFileExtension(it.name))) {
                sourceFiles.add(it)
            }
        }

        def parseCount = new AtomicInteger(0)
        GParsPool.withPool {
            sourceFiles.stream().limit(parseFileLimit).collect(Collectors.toList()).eachParallel {
                def file = it as File
                if (!file.exists()) {
                    System.err.println("Skipping non-existent file: " + file)
                    return
                }

                println "Parsing: " + file
                def resp = client.parse(file.name, file.text, observedLanguage.language.key, Encoding.UTF8$.MODULE$)
                if (resp == null) {
                    System.err.println "Got null parse response"
                    //todo: understand this
                } else {
                    def rootSourceNode = new SourceNode(observedLanguage.language, resp.uast)
                    if (filter.evaluate(rootSourceNode)) {
                        observeSourceNode(observedLanguage, rootSourceNode)
                    }
                    extractSchema(observedLanguage, rootSourceNode, rootSourceNode.children)
                    parseCount.getAndIncrement()
                }
            }
        }
        println "Parsed " + parseCount.get() + " " + observedLanguage.language.qualifiedName + " files"
    }

    private void extractSchema(ObservedLanguage observedLanguage, SourceNode parentNode,
                               Iterator<SourceNode> childNodes) {
        childNodes.each {
            if (filter.evaluate(it)) {
                extractSchema(observedLanguage, it, it.children)
                observeSourceNode(observedLanguage, it)

                //parent and child don't relate in any way besides parent/child
                if (!parentNode.underlyingNode.children().contains(it.underlyingNode)) {
                    observedLanguage.observeParentChildRelation(parentNode.internalType, it)
                }
            } else {
                extractSchema(observedLanguage, parentNode, it.children)
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
}
