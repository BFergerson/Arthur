package com.codebrig.arthur.observe.structure.literal

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.LiteralFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.NameFilter
import com.codebrig.arthur.observe.structure.filter.operator.relational.define.InitializeVariableOperatorFilter
import org.bblfsh.client.v2.BblfshClient
import org.junit.Test

import static org.junit.Assert.*

class PhpLiteralTest extends ArthurTest {

    @Test
    void intLiteralTest() {
        assertPhpLiteralPresent("param1", "numberValue", 1)
    }

    @Test
    void stringLiteralTest() {
        assertPhpLiteralPresent("param2", "stringValue", "stringParam2")
    }

    @Test
    void binaryLiteralTest() {
        assertPhpLiteralPresent("param3", "numberValue", 0b01111111100000000000000000000000)
        assertPhpLiteralPresent("param8", "numberValue", -0b01111111100000000000000000000000)
    }

    @Test
    void octalLiteralTest() {
        assertPhpLiteralPresent("param4", "numberValue", 0777)
    }

    @Test
    void hexadecimalLiteralTest() {
        assertPhpLiteralPresent("param5", "doubleValue", 0xFFFFFFFFFFFFFFFF)
        assertPhpLiteralPresent("param9", "numberValue", -0x123456789ABCDEF)
    }

    @Test
    void signedEngineeringNotationLiteralTest() {
        assertPhpLiteralPresent("param6", "doubleValue", 7E-10)
        assertPhpLiteralPresent("param7", "doubleValue", -1e-1)
    }


    private static void assertPhpLiteralPresent(String literalName, String literalType, Object literalValue) {
        def file = new File("src/test/resources/same/literals/Literals.php")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.babelfishName)
        def rootNode = new BblfshClient.UastMethods(resp.uast()).decode().root().load()

        boolean foundLiteral = false
        MultiFilter.matchAll(new NameFilter(literalName), new InitializeVariableOperatorFilter())
                .getFilteredNodes(language, rootNode).each {
            assertEquals(literalName, it.name)
            def literalNode = new LiteralFilter().getFilteredNodes(it).next()
            assertNotNull(literalNode)
            assertEquals(literalType, literalNode.getLiteralAttribute())
            if (literalNode.getLiteralAttribute() == PhpLiteral.stringValueLiteral()) {
                assertEquals(literalValue, literalNode.getLiteralValue())
            } else {
                assertEquals(literalValue as double, literalNode.getLiteralValue() as double, 0.0)
            }
            foundLiteral = true
        }
        assertTrue(foundLiteral)
    }
}
