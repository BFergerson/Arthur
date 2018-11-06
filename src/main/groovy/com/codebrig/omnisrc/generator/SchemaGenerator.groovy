package com.codebrig.omnisrc.generator

import com.codebrig.omnisrc.SourceLanguage
import com.codebrig.omnisrc.observations.ObservedLanguage
import com.codebrig.omnisrc.schema.grakn.GraknSchemaWriter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import gopkg.in.bblfsh.sdk.v1.protocol.generated.ParseResponse
import groovy.io.FileType
import groovyx.gpars.GParsPool
import org.bblfsh.client.BblfshClient
import org.eclipse.jgit.api.Git
import org.kohsuke.github.GHDirection
import org.kohsuke.github.GHRepositorySearchBuilder
import org.kohsuke.github.GitHub
import scala.collection.JavaConverters

import java.util.concurrent.TimeUnit
import java.util.stream.Collectors

import static com.google.common.io.Files.getFileExtension

/**
 * todo: description
 *
 * @version 0.2
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class SchemaGenerator {

    public static final int PARSE_FILES_PER_PROJECT = 1000

    static void generateUnilingualSchema(SourceLanguage language, int parseProjectCount, File outputFile) {
        def client = new BblfshClient("0.0.0.0", 9432, Integer.MAX_VALUE)
        def schemaWriter = new GraknSchemaWriter(observeLanguage(client, language, parseProjectCount))
        if (outputFile.exists()) outputFile.delete()
        outputFile.createNewFile()
        outputFile.write(schemaWriter.getSchemaDefinition())
    }

    static ObservedLanguage observeLanguage(BblfshClient client, SourceLanguage language, int parseProjectCount) {
        def observedLanguage = new ObservedLanguage(language)
        int parsedProjects = 0
        GitHub.connectAnonymously().searchRepositories()
                .sort(GHRepositorySearchBuilder.Sort.STARS)
                .order(GHDirection.DESC)
                .language(language.key())
                .list().iterator().find {
            if (parsedProjects++ >= parseProjectCount) return true

            analyzeGithubRepositoryObservation(client, it.fullName, observedLanguage)
            return false
        }
        return observedLanguage
    }

    static void analyzeGithubRepositoryObservation(BblfshClient client, String repoName,
                                                   ObservedLanguage observedLanguage) {
        println "Analyzing repository: $repoName"
        def outputFolder = new File("/tmp/omnisrc/out/$repoName")
        if (new File(outputFolder, "cloned.omnisrc").exists()) {
            println "$repoName already exists. Skipping"
        } else {
            if (outputFolder.exists()) outputFolder.deleteDir()
            outputFolder.mkdirs()
            cloneRepo(repoName, outputFolder)
        }

        def sourceFiles = new ArrayList<File>()
        outputFolder.eachFileRecurse(FileType.FILES) {
            if (observedLanguage.language.isValidExtension(getFileExtension(it.name))) {
                sourceFiles.add(it)
            }
        }
        def responseList = new ArrayList<ParseResponse>()
        GParsPool.withPool {
            sourceFiles.stream().limit(PARSE_FILES_PER_PROJECT).collect(Collectors.toList()).eachParallel {
                println "Parsing: " + it
                responseList.add(client.parse(it.name, it.text, observedLanguage.language.key(), Encoding.UTF8$.MODULE$))
            }
        }

        println "Extracting observed attributes, relations, and roles"
        responseList.each { resp ->
            if (resp == null) {
                System.err.println "Got null parse response"
                //todo: understand this
            } else {
                asJavaIterator(BblfshClient.iterator(resp.uast, BblfshClient.PreOrder())).each {
                    if (it != null && !it.internalType().isEmpty()) {
                        //attributes
                        observedLanguage.observeAttributes(it.internalType(), asJavaMap(it.properties()))
                        //relations
                        observedLanguage.observeRelations(it.internalType(), asJavaIterator(it.children()))
                        //roles
                        observedLanguage.observeRoles(it.internalType(), asJavaIterator(it.roles()))
                    }
                }
            }
        }
        println "Parsed " + responseList.size() + " " + observedLanguage.language.qualifiedName + " files"
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

    static <T> Iterator<T> asJavaIterator(scala.collection.Iterator<T> scalaIterator) {
        return JavaConverters.asJavaIteratorConverter(scalaIterator).asJava()
    }

    static <T> Iterator<T> asJavaIterator(scala.collection.Iterable<T> scalaIterator) {
        return JavaConverters.asJavaCollectionConverter(scalaIterator).asJavaCollection().iterator()
    }

    static Map<String, String> asJavaMap(scala.collection.Map<String, String> scalaMap) {
        return JavaConverters.mapAsJavaMapConverter(scalaMap).asJava()
    }
}