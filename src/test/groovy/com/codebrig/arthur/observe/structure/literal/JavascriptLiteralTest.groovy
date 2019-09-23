package com.codebrig.arthur.observe.structure.literal

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.LiteralFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.NameFilter
import com.codebrig.arthur.observe.structure.filter.operator.relational.define.InitializeVariableOperatorFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.apache.commons.lang.StringEscapeUtils
import org.junit.Test

import static org.junit.Assert.*

class JavascriptLiteralTest extends ArthurTest {

    @Test
    void intLiteralTest() {
        assertJavascriptLiteralPresent("param1", "numberValue", 1)
        assertJavascriptLiteralPresent("param2", "numberValue", 2)
        assertJavascriptLiteralPresent("param3", "numberValue", 3)
        assertJavascriptLiteralPresent("param6", "numberValue", Integer.parseInt("0888"))
    }

    @Test
    void stringLiteralTest() {
        assertJavascriptLiteralPresent("param4", "stringValue", StringEscapeUtils.escapeJava("\"stringParam4\""))
    }

    @Test
    void octalLiteralTest() {
        assertJavascriptLiteralPresent("param5", "numberValue", 0777)
        assertJavascriptLiteralPresent("param7", "numberValue", "0o10")
        assertJavascriptLiteralPresent("param8", "numberValue", "-0o10")
    }

    @Test
    void binaryLiteralTest() {
        assertJavascriptLiteralPresent("param9", "numberValue", 0b01111111100000000000000000000000)
        assertJavascriptLiteralPresent("param10", "numberValue", 0B00000000011111111111111111111111)
    }

    @Test
    void hexadecimalLiteralTest() {
        assertJavascriptLiteralPresent("param11", "numberValue", 0x123456789ABCDEF)
        assertJavascriptLiteralPresent("param12", "doubleValue", 0xFFFFFFFFFFFFFFFF)
    }

    @Test
    void signedEngineeringNotationLiteralTest() {
        assertJavascriptLiteralPresent("param13", "doubleValue", -1.2e55)
        assertJavascriptLiteralPresent("param14", "doubleValue", 1.2e-55)
        assertJavascriptLiteralPresent("param15", "doubleValue", -1.2e-55)
        assertJavascriptLiteralPresent("param16", "numberValue", 0.1E3)
    }

    private static void assertJavascriptLiteralPresent(String literalName, String literalType, Object literalValue) {
        def file = new File("src/test/resources/same/literals/Literals.js")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.babelfishName, Encoding.UTF8$.MODULE$)

        boolean foundLiteral = false
        MultiFilter.matchAll(new NameFilter(literalName), new InitializeVariableOperatorFilter())
                .getFilteredNodes(language, resp.uast).each {
            assertEquals(literalName, it.name)
            def literalNode = new LiteralFilter().getFilteredNodes(it).next()
            assertNotNull(literalNode)
            assertEquals(literalType, literalNode.getLiteralAttribute())
            if (literalNode.getLiteralAttribute() == JavascriptLiteral.stringValueLiteral()) {
                assertEquals(literalValue, literalNode.getLiteralValue())
            } else if (JavascriptLiteral.isOctalLiteral(literalValue as String)) {
                assertEquals(JavascriptLiteral.getOctalValue(literalNode) as double, literalNode.getLiteralValue() as double, 0.0)
            } else {
                assertEquals(literalValue as double, literalNode.getLiteralValue() as double, 0.0)
            }
            foundLiteral = true
        }
        assertTrue(foundLiteral)
    }
}
