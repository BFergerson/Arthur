package com.codebrig.omnisrc.schema.grakn

import com.codebrig.omnisrc.OmniSRCTest
import com.codebrig.omnisrc.SourceLanguage
import com.codebrig.omnisrc.generator.SchemaGenerator
import com.codebrig.omnisrc.observe.ObservationConfig
import com.codebrig.omnisrc.observe.ObservedLanguages
import com.codebrig.omnisrc.schema.SchemaSegment
import org.junit.Test

import static org.junit.Assert.assertEquals

class GraknSchemaWriterTest extends OmniSRCTest {

    @Test
    void segmentedSchema() {
        def schemaGenerator = new SchemaGenerator(ObservationConfig.fullStructure())
        def goLanguage = schemaGenerator.observeLanguage(SourceLanguage.Go, new File("src/test/resources/same/program/"))
        def javaLanguage = schemaGenerator.observeLanguage(SourceLanguage.Java, new File("src/test/resources/same/program/"))
        def javascriptLanguage = schemaGenerator.observeLanguage(SourceLanguage.Javascript, new File("src/test/resources/same/program/"))
        def phpLanguage = schemaGenerator.observeLanguage(SourceLanguage.Php, new File("src/test/resources/same/program/"))
        def pythonLanguage = schemaGenerator.observeLanguage(SourceLanguage.Python, new File("src/test/resources/same/program/"))
        def rubyLanguage = schemaGenerator.observeLanguage(SourceLanguage.Ruby, new File("src/test/resources/same/program/"))
        def omniLanguage = ObservedLanguages.mergeLanguages(goLanguage, javaLanguage, javascriptLanguage, phpLanguage, pythonLanguage, rubyLanguage)
        def schemaWriter = new GraknSchemaWriter(omniLanguage, goLanguage, javaLanguage, javascriptLanguage, phpLanguage, pythonLanguage, rubyLanguage)

        def baseStructure = schemaWriter.getSegmentedSchemaDefinition(ObservationConfig.baseStructure().asArray())
        assertEquals(new File("src/test/resources/schema/segment_same", "Base_Structure.gql").text, baseStructure)
        def individualSemanticRoles = schemaWriter.getSegmentedSchemaDefinition(SchemaSegment.INDIVIDUAL_SEMANTIC_ROLES)
        assertEquals(new File("src/test/resources/schema/segment_same", "Individual_Roles.gql").text, individualSemanticRoles)
        def actualSemanticRoles = schemaWriter.getSegmentedSchemaDefinition(SchemaSegment.ACTUAL_SEMANTIC_ROLES)
        assertEquals(new File("src/test/resources/schema/segment_same", "Actual_Roles.gql").text, actualSemanticRoles)
    }
}