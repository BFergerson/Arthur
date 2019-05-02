package com.codebrig.arthur.generator.github

import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.generator.SchemaGenerator
import com.codebrig.arthur.observe.ObservationConfig
import com.codebrig.arthur.schema.SchemaSegment
import com.codebrig.arthur.schema.SegmentedSchemaConfig
import com.codebrig.arthur.schema.grakn.GraknSchemaWriter
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.util.concurrent.TimeUnit

/**
 * Generate a single language schema by observing source code on GitHub
 *
 * @version 0.3.2
 * @since 0.1
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class UnilingualGithubSchemaGenerator extends SchemaGenerator {

    private static final Logger log = LoggerFactory.getLogger(this.name)

    static void main(String[] args) {
        def language = SourceLanguage.getSourceLanguageByName(args[0])
        def parseProjectsCount = args[1] as int
        long startTime = System.currentTimeMillis()

        def schemaGenerator = new SchemaGenerator(ObservationConfig.fullStructure())
        def schemaWriter = new GraknSchemaWriter(schemaGenerator.observeLanguage(language, parseProjectsCount))

        log.info "Writing segmented Grakn schema"
        schemaWriter.storeSegmentedSchemaDefinition(new SegmentedSchemaConfig()
                .withFileSegment(new File("src/main/resources/schema/unilingual/" + language.key,
                "Arthur_" + language.qualifiedName + "_Base_Structure.gql"), ObservationConfig.baseStructure().asArray())
                .withFileSegment(new File("src/main/resources/schema/unilingual/" + language.key,
                "Arthur_" + language.qualifiedName + "_Semantic_Roles.gql"), SchemaSegment.SEMANTIC_ROLES))
        log.info "Completed in: " + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s"
    }
}
