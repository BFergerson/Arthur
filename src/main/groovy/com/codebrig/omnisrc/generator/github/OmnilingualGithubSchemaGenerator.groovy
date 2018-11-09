package com.codebrig.omnisrc.generator.github

import com.codebrig.omnisrc.SourceLanguage
import com.codebrig.omnisrc.generator.SchemaGenerator
import com.codebrig.omnisrc.observations.ObservedLanguage
import com.codebrig.omnisrc.observations.OmniObservedLanguage
import com.codebrig.omnisrc.schema.io.grakn.GraknSchemaWriter

import java.util.concurrent.TimeUnit

/**
 * todo: description
 *
 * @version 0.2
 * @since 0.1
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class OmnilingualGithubSchemaGenerator extends SchemaGenerator {

    public static final int PARSE_PROJECTS_PER_LANGUAGE = 3
    public static final int PARSE_FILES_PER_PROJECT = 1000

    static void main(String[] args) {
        long startTime = System.currentTimeMillis()

        def schemaGenerator = new SchemaGenerator()
        def observedLanguages = new ArrayList<ObservedLanguage>()
        SourceLanguage.values().each {
            if (it != SourceLanguage.OmniSRC) {
                observedLanguages.add(schemaGenerator.observeLanguage(it, PARSE_PROJECTS_PER_LANGUAGE, PARSE_FILES_PER_PROJECT))
            }
        }

        def omniLanguage = OmniObservedLanguage.makeOmniObservedLanguage(observedLanguages)
        def schemaWriter = new GraknSchemaWriter(omniLanguage, observedLanguages.toArray(new ObservedLanguage[0]))
        def outputFile = new File("src/main/resources/schema/omnilingual/OmniSRC_" + SourceLanguage.OmniSRC.qualifiedName + "_Schema.gql")
        if (outputFile.exists()) outputFile.delete()
        outputFile.createNewFile()
        outputFile.write(schemaWriter.getSchemaDefinition())
        println "Completed in: " + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s"
    }
}
