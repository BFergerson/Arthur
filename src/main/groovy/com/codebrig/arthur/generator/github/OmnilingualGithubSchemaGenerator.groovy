package com.codebrig.arthur.generator.github

import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.generator.SchemaGenerator
import com.codebrig.arthur.observe.ObservationConfig
import com.codebrig.arthur.observe.ObservedLanguage
import com.codebrig.arthur.observe.ObservedLanguages
import com.codebrig.arthur.schema.SchemaSegment
import com.codebrig.arthur.schema.SegmentedSchemaConfig
import com.codebrig.arthur.schema.grakn.GraknSchemaWriter

import java.util.concurrent.TimeUnit

/**
 * Generate a multi-language schema by observing source code on GitHub
 *
 * @version 0.4
 * @since 0.1
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class OmnilingualGithubSchemaGenerator extends SchemaGenerator {

    public static final int PARSE_PROJECTS_PER_LANGUAGE = 75
    public static final int PARSE_FILES_PER_PROJECT = Integer.MAX_VALUE

    static void main(String[] args) {
        long startTime = System.currentTimeMillis()
        def schemaGenerator = new SchemaGenerator(ObservationConfig.fullStructure())
        def observedLanguages = new ArrayList<ObservedLanguage>()
        SourceLanguage.values().each {
            if (it != SourceLanguage.Omnilingual) {
                observedLanguages.add(schemaGenerator.observeLanguage(it, PARSE_PROJECTS_PER_LANGUAGE, PARSE_FILES_PER_PROJECT))
            }
        }

        println "Merging common language features"
        def omniLanguage = ObservedLanguages.mergeLanguages(observedLanguages)

        println "Writing segmented Grakn schema"
        def schemaWriter = new GraknSchemaWriter(omniLanguage, observedLanguages.toArray(new ObservedLanguage[0]))
        schemaWriter.storeSegmentedSchemaDefinition(new SegmentedSchemaConfig()
                .withFileSegment(new File("src/main/resources/schema/omnilingual/",
                "Arthur_" + SourceLanguage.Omnilingual.qualifiedName + "_Base_Structure.gql"), ObservationConfig.baseStructure().asArray())
                .withFileSegment(new File("src/main/resources/schema/omnilingual/",
                "Arthur_" + SourceLanguage.Omnilingual.qualifiedName + "_Semantic_Roles.gql"), SchemaSegment.SEMANTIC_ROLES))
        println "Completed in: " + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s"
    }
}
