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
    }

    @Test
    void stringLiteralTest() {
        assertJavascriptLiteralPresent("param4", "stringValue", StringEscapeUtils.escapeJava("\"stringParam4\""))
    }

    private static void assertJavascriptLiteralPresent(String literalName, String literalType, Object literalValue) {
        def file = new File("src/test/resources/same/literals/Literals.js")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        boolean foundLiteral = false
        MultiFilter.matchAll(new NameFilter(literalName), new InitializeVariableOperatorFilter())
                .getFilteredNodes(language, resp.uast).each {
            assertEquals(literalName, it.name)
            def literalNode = new LiteralFilter().getFilteredNodes(it).next()
            assertNotNull(literalNode)
            assertEquals(literalType, literalNode.getLiteralAttribute())
            if (literalNode.getLiteralAttribute() == JavascriptLiteral.stringValueLiteral()) {
                assertEquals(literalValue, literalNode.getLiteralValue())
            } else {
                assertEquals(literalValue as double, literalNode.getLiteralValue() as double, 0.0)
            }
            foundLiteral = true
        }
        assertTrue(foundLiteral)
    }
}
