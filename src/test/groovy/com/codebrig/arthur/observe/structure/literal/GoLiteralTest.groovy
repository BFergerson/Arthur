package com.codebrig.arthur.observe.structure.literal

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.LiteralFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.NameFilter
import com.codebrig.arthur.observe.structure.filter.operator.relational.define.InitializeVariableOperatorFilter
import org.apache.commons.text.StringEscapeUtils
import org.bblfsh.client.v2.BblfshClient
import org.junit.Test

import static org.junit.Assert.*

/**
 * Note: Go v1.13 will add support for more types such as binary literals and hexadecimal floating point literals.
 */
class GoLiteralTest extends ArthurTest {

    @Test
    void intLiteralTest() {
        assertGoLiteralPresent("param1", "numberValue", 1)
    }

    @Test
    void stringLiteralTest() {
        assertGoLiteralPresent("param2", "stringValue", StringEscapeUtils.escapeJava("\"stringParam2\""))
    }

    @Test
    void signedEngineeringNotationLiteralTest() {
        assertGoLiteralPresent("param8", "doubleValue", Double.parseDouble('123.E+2'))
        assertGoLiteralPresent("param9", "doubleValue", 1e-1)
        assertGoLiteralPresent("param10", "doubleValue", -1e-1)
    }

    @Test
    void hexadecimalLiteralTest() {
        assertGoLiteralPresent("param3", "numberValue", 0xFFFFFFFFFFFF)
        assertGoLiteralPresent("param4", "numberValue", 0xff1a618b7f65ea12L)
        assertGoLiteralPresent("param5", "numberValue", 0xc4ceb9fe1a85ec53L)
    }

    @Test
    void octalLiteralTest() {
        assertGoLiteralPresent("param6", "numberValue", 0757)
        assertGoLiteralPresent("param7", "numberValue", 0123546263753256452432)
    }

    private static void assertGoLiteralPresent(String literalName, String literalType, Object literalValue) {
        def file = new File("src/test/resources/same/literals/Literals.go")
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
            if (literalNode.getLiteralAttribute() == GoLiteral.stringValueLiteral()) {
                assertEquals(literalValue, literalNode.getLiteralValue())
            } else {
                assertEquals(literalValue as double, literalNode.getLiteralValue() as double, 0.0)
            }
            foundLiteral = true
        }
        assertTrue(foundLiteral)
    }
}
