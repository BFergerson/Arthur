package com.codebrig.omnisrc.generators

import com.codebrig.omnisrc.SourceLanguage
import com.codebrig.omnisrc.observations.ObservedLanguage
import com.codebrig.omnisrc.observations.OmniObservedLanguage
import com.codebrig.omnisrc.schema.grakn.GraknSchemaWriter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import groovy.io.FileType
import org.bblfsh.client.BblfshClient
import org.eclipse.jgit.api.Git
import org.kohsuke.github.GHDirection
import org.kohsuke.github.GHRepositorySearchBuilder
import org.kohsuke.github.GitHub
import scala.collection.JavaConverters

import java.util.concurrent.TimeUnit

import static com.google.common.io.Files.*

/**
 * @author github.com/BFergerson
 */
class OmniSchemaGenerator {

    public static final int PARSE_PROJECTS_PER_LANGUAGE = 3

    static void main(String[] args) {
        long startTime = System.currentTimeMillis()
        def client = new BblfshClient("0.0.0.0", 9432, Integer.MAX_VALUE)

        def observedLanguages = new ArrayList<ObservedLanguage>()
        SourceLanguage.values().each {
            if (it != SourceLanguage.OmniSRC) {
                observedLanguages.add(observeLanguage(client, it, PARSE_PROJECTS_PER_LANGUAGE))
            }
        }

        def omniLanguage = new OmniObservedLanguage()
        observedLanguages.each { lang ->
            lang.observedEntities.each { entity ->
                observedLanguages.stream().each {
                    if (lang.language != it.language && it.observedEntity(entity)) {
                        omniLanguage.observeGlobalEntity(entity)
                        observedLanguages.each {
                            it.addEntityExtends(entity)
                        }
                    }
                }
            }
            lang.observedAttributes.each { attribute ->
                observedLanguages.stream().each {
                    if (lang.language != it.language && it.observedAttribute(attribute)) {
                        omniLanguage.observeGlobalAttribute(attribute)
                        observedLanguages.each {
                            it.addAttributeExtends(attribute)
                        }
                    }
                }
            }
            lang.observedRelations.each { relation ->
                observedLanguages.stream().each {
                    if (lang.language != it.language && it.observedRelation(relation)) {
                        omniLanguage.observeGlobalRelation(relation)
                        observedLanguages.each {
                            it.addRelationExtends(relation)
                        }
                    }
                }
            }
            lang.observedRoles.each { role ->
                omniLanguage.observeGlobalRole(role)
            }
        }
        omniLanguage.observedRoles.each { role ->
            observedLanguages.each { lang ->
                lang.getEntitiesWithRole(role).each { entity ->
                    observedLanguages.each {
                        if (lang.language != it.language && it.observedEntityRole(entity, role)) {
                            omniLanguage.addEntityRole(entity, role)
                            it.removeEntityRole(entity, role)
                            lang.removeEntityRole(entity, role)
                        }
                    }
                }
            }
        }

        def schemaWriter = new GraknSchemaWriter(omniLanguage, observedLanguages.toArray(new ObservedLanguage[0]))
        def outputFile = new File("src/main/resources/schema/omnilingual/OmniSRC_" + SourceLanguage.OmniSRC.qualifiedName + "_Schema.gql")
        if (outputFile.exists()) outputFile.delete()
        outputFile.createNewFile()
        outputFile.write(schemaWriter.getSchemaDefinition())
        println "Completed in: " + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s"
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

            doGithubRepositoryObservation(client, it.fullName, observedLanguage)
            return false
        }
        return observedLanguage
    }

    static void generateUnilingualSchema(SourceLanguage language, int parseProjectCount, File outputFile) {
        def client = new BblfshClient("0.0.0.0", 9432, Integer.MAX_VALUE)
        def schemaWriter = new GraknSchemaWriter(observeLanguage(client, language, parseProjectCount))
        if (outputFile.exists()) outputFile.delete()
        outputFile.createNewFile()
        outputFile.write(schemaWriter.getSchemaDefinition())
    }

    static void doGithubRepositoryObservation(BblfshClient client, String repoName, ObservedLanguage observedLanguage) {
        println "Doing repo: $repoName"
        def outputFolder = new File("/tmp/omnisrc/out/$repoName")
        if (new File(outputFolder, "cloned.omnisrc").exists()) {
            println "$repoName already exists. Skipping"
        } else {
            if (outputFolder.exists()) outputFolder.deleteDir()
            outputFolder.mkdirs()
            cloneRepo(repoName, outputFolder)
        }

        int parsedFileCount = 0
        outputFolder.eachFileRecurse(FileType.FILES) {
            if (observedLanguage.language.isValidExtension(getFileExtension(it.name))) {
                println "Parsing: " + it
                parsedFileCount++
                def fileContent = it.text
                def resp = client.parse(it.name, fileContent, observedLanguage.language.key(), Encoding.UTF8$.MODULE$)
                //todo: error handling

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
        println "Parsed $parsedFileCount " + observedLanguage.language.qualifiedName + " files"
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
