package com.codebrig.arthur.generator

import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.ObservationConfig
import com.codebrig.arthur.observe.ObservedLanguages
import com.codebrig.arthur.observe.structure.filter.CompilationUnitFilter
import com.codebrig.arthur.observe.structure.filter.FunctionFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.schema.grakn.GraknSchemaWriter
import groovy.util.logging.Slf4j
import org.apache.commons.lang3.StringUtils
import org.junit.Test

import static org.junit.Assert.assertEquals

@Slf4j
class SchemaGeneratorTest {

    @Test
    void fileAndFunctionOnlySchema() {
        def schemaGenerator = new SchemaGenerator(ObservationConfig.baseStructureWithSemanticRoles())
        def multiFilter = new MultiFilter(MultiFilter.MatchStyle.ANY)
        multiFilter.accept(new CompilationUnitFilter())
        multiFilter.accept(new FunctionFilter())
        schemaGenerator.filter = multiFilter

        def goLanguage = schemaGenerator.observeLanguage(SourceLanguage.Go, new File("../src/test/resources/same/program/"))
        def javaLanguage = schemaGenerator.observeLanguage(SourceLanguage.Java, new File("src/test/resources/same/program/"))
        def javascriptLanguage = schemaGenerator.observeLanguage(SourceLanguage.Javascript, new File("src/test/resources/same/program/"))
        def phpLanguage = schemaGenerator.observeLanguage(SourceLanguage.Php, new File("src/test/resources/same/program/"))
        def pythonLanguage = schemaGenerator.observeLanguage(SourceLanguage.Python, new File("src/test/resources/same/program/"))
        def rubyLanguage = schemaGenerator.observeLanguage(SourceLanguage.Ruby, new File("src/test/resources/same/program/"))
        def cSharpLanguage = schemaGenerator.observeLanguage(SourceLanguage.CSharp, new File("src/test/resources/same/program/"))
        def bashLanguage = schemaGenerator.observeLanguage(SourceLanguage.Bash, new File("src/test/resources/same/program/"))
        def cppLanguage = schemaGenerator.observeLanguage(SourceLanguage.CPlusPlus, new File("src/test/resources/same/program/"))
        def omniLanguage = ObservedLanguages.mergeLanguages(goLanguage, javaLanguage, javascriptLanguage, phpLanguage, pythonLanguage, rubyLanguage, cSharpLanguage, bashLanguage, cppLanguage)
        def schemaWriter = new GraknSchemaWriter(omniLanguage, goLanguage, javaLanguage, javascriptLanguage, phpLanguage, pythonLanguage, rubyLanguage, cSharpLanguage, bashLanguage, cppLanguage)
        log.warn "Working Directory = " + System.getProperty("user.dir")
        log.warn "File: " + new File("src/test/resources/schema/", "Same_Schema.gql").getAbsoluteFile()
        def verifyFile = new File("src/test/resources/schema/", "Same_Schema.gql")
        log.warn "verifyFile.text: '" + verifyFile.text + "'"
        log.warn "schemaWriter.fullSchemaDefinition: '" + schemaWriter.fullSchemaDefinition + "'"
        assertEquals(verifyFile.text, schemaWriter.fullSchemaDefinition)
    }
}