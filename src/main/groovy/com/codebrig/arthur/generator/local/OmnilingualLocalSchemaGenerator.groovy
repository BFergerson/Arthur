package com.codebrig.arthur.generator.local

import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.generator.SchemaGenerator
import com.codebrig.arthur.observe.ObservationConfig
import com.codebrig.arthur.observe.ObservedLanguage
import com.codebrig.arthur.observe.ObservedLanguages
import com.codebrig.arthur.schema.SchemaSegment
import com.codebrig.arthur.schema.SegmentedSchemaConfig
import com.codebrig.arthur.schema.grakn.GraknSchemaWriter
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.util.concurrent.TimeUnit

/**
 * Generate a multi-language schema by observing local source code
 *
 * @version 0.3.2
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class OmnilingualLocalSchemaGenerator extends SchemaGenerator {

    private static final Logger log = LoggerFactory.getLogger(this.name)

    static void main(String[] args) {
        def inputDirectory = new File(args[0])
        long startTime = System.currentTimeMillis()
        def schemaGenerator = new SchemaGenerator(ObservationConfig.fullStructure())
        def observedLanguages = new ArrayList<ObservedLanguage>()
        SourceLanguage.values().each {
            if (it != SourceLanguage.Omnilingual) {
                observedLanguages.add(schemaGenerator.observeLanguage(it, inputDirectory))
            }
        }

        log.info "Merging common language features"
        def omniLanguage = ObservedLanguages.mergeLanguages(observedLanguages)

        log.info "Writing segmented Grakn schema"
        def schemaWriter = new GraknSchemaWriter(omniLanguage, observedLanguages.toArray(new ObservedLanguage[0]))
        schemaWriter.storeSegmentedSchemaDefinition(new SegmentedSchemaConfig()
                .withFileSegment(new File("src/main/resources/schema/omnilingual/",
                "Arthur_" + SourceLanguage.Omnilingual.qualifiedName + "_Base_Structure.gql"), ObservationConfig.baseStructure().asArray())
                .withFileSegment(new File("src/main/resources/schema/omnilingual/",
                "Arthur_" + SourceLanguage.Omnilingual.qualifiedName + "_Semantic_Roles.gql"), SchemaSegment.SEMANTIC_ROLES))
        log.info "Completed in: " + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s"
    }
}
