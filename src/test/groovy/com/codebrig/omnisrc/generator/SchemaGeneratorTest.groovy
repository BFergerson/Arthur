package com.codebrig.omnisrc.generator

import com.codebrig.omnisrc.SourceLanguage
import com.codebrig.omnisrc.observations.OmniObservedLanguage
import com.codebrig.omnisrc.schema.grakn.GraknSchemaWriter
import com.codebrig.omnisrc.structure.filter.MultiFilter
import com.codebrig.omnisrc.structure.filter.RoleFilter
import com.codebrig.omnisrc.structure.filter.TypeFilter
import org.junit.Test

class SchemaGeneratorTest {

    @Test
    void fileAndFunctionOnlySchema() {
        def schemaGenerator = new SchemaGenerator()
        def multiFilter = new MultiFilter()
        def roleFilter = new RoleFilter("FILE", "DECLARATION_FUNCTION")
        multiFilter.acceptFilter(roleFilter)
        multiFilter.acceptFilter(new TypeFilter("MethodDeclaration"))
        schemaGenerator.filter = multiFilter

        def goLanguage = schemaGenerator.observeLanguage(SourceLanguage.Go,
                new File("/home/brandon/IdeaProjects/OmniSRC/src/test/resources/same"))
        def javaLanguage = schemaGenerator.observeLanguage(SourceLanguage.Java,
                new File("/home/brandon/IdeaProjects/OmniSRC/src/test/resources/same"))
        def javascriptLanguage = schemaGenerator.observeLanguage(SourceLanguage.Javascript,
                new File("/home/brandon/IdeaProjects/OmniSRC/src/test/resources/same"))
        def pythonLanguage = schemaGenerator.observeLanguage(SourceLanguage.Python,
                new File("/home/brandon/IdeaProjects/OmniSRC/src/test/resources/same"))
        def omniLanguage = OmniObservedLanguage.makeOmniObservedLanguage(goLanguage, javaLanguage, javascriptLanguage, pythonLanguage)
        def schemaWriter = new GraknSchemaWriter(omniLanguage, goLanguage, javaLanguage, javascriptLanguage, pythonLanguage)
        def outputFile = new File("/home/brandon/IdeaProjects/OmniSRC/src/test/resources/same/test.gql")
        if (outputFile.exists()) outputFile.delete()
        outputFile.createNewFile()
        outputFile.write(schemaWriter.getSchemaDefinition())
    }

}