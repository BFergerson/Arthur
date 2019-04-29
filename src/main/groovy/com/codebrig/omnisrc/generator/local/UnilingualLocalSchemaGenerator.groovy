package com.codebrig.omnisrc.generator.local

import com.codebrig.omnisrc.SourceLanguage
import com.codebrig.omnisrc.generator.SchemaGenerator
import com.codebrig.omnisrc.observe.ObservationConfig
import com.codebrig.omnisrc.schema.SchemaSegment
import com.codebrig.omnisrc.schema.SegmentedSchemaConfig
import com.codebrig.omnisrc.schema.grakn.GraknSchemaWriter

import java.util.concurrent.TimeUnit

/**
 * Generate a single language schema by observing local source code
 *
 * @version 0.3.2
 * @since 0.1
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class UnilingualLocalSchemaGenerator extends SchemaGenerator {

    static void main(String[] args) {
        def language = SourceLanguage.getSourceLanguageByName(args[0])
        def inputDirectory = args[1] as File
        long startTime = System.currentTimeMillis()

        def schemaGenerator = new SchemaGenerator(ObservationConfig.fullStructure())
        def schemaWriter = new GraknSchemaWriter(schemaGenerator.observeLanguage(language, inputDirectory))

        println "Writing segmented Grakn schema"
        schemaWriter.storeSegmentedSchemaDefinition(new SegmentedSchemaConfig()
                .withFileSegment(new File("src/main/resources/schema/unilingual/" + language.key,
                "OmniSRC_" + language.qualifiedName + "_Base_Structure.gql"), ObservationConfig.baseStructure().asArray())
                .withFileSegment(new File("src/main/resources/schema/unilingual/" + language.key,
                "OmniSRC_" + language.qualifiedName + "_Individual_Semantic_Roles.gql"), SchemaSegment.INDIVIDUAL_SEMANTIC_ROLES)
                .withFileSegment(new File("src/main/resources/schema/unilingual/" + language.key,
                "OmniSRC_" + language.qualifiedName + "_Actual_Semantic_Roles.gql"), SchemaSegment.ACTUAL_SEMANTIC_ROLES))
        println "Completed in: " + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s"
    }
}
