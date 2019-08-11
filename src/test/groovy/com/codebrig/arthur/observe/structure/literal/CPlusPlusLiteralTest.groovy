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

class CPlusPlusLiteralTest extends ArthurTest {

    @Test
    void intLongLiteralTest() {
        assertCPlusPlusLiteralPresent("param1", "numberValue", 100)
        assertCPlusPlusLiteralPresent("param2", "numberValue", 200)
        assertCPlusPlusLiteralPresent("param3", "numberValue", "300")
        assertCPlusPlusLiteralPresent("param4", "numberValue", 400L)
        assertCPlusPlusLiteralPresent("param5", "numberValue", "-500")
        assertCPlusPlusLiteralPresent("param6", "numberValue", "600")
        assertCPlusPlusLiteralPresent("param7", "numberValue", 700)
        assertCPlusPlusLiteralPresent("param8", "numberValue", "800")
        assertCPlusPlusLiteralPresent("param9", "numberValue", "-900")
        assertCPlusPlusLiteralPresent("param10", "numberValue", 1000)
        assertCPlusPlusLiteralPresent("param11", "numberValue", "1100")
        assertCPlusPlusLiteralPresent("param12", "numberValue", "1200")
    }

    @Test
    void stringLiteralTest() {
        assertCPlusPlusLiteralPresent("param13", "stringValue", StringEscapeUtils.escapeJava("\"stringParam13\""))
    }

    private static void assertCPlusPlusLiteralPresent(String literalName, String literalType, Object literalValue) {
        def file = new File("src/test/resources/same/literals/Literals.cpp")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.babelfishName, Encoding.UTF8$.MODULE$)

        boolean foundLiteral = false
        MultiFilter.matchAll(new NameFilter(literalName), new InitializeVariableOperatorFilter())
                .getFilteredNodes(language, resp.uast).each {
            assertEquals(literalName, it.name)
            def literalNode = new LiteralFilter().getFilteredNodes(it).next()
            assertNotNull(literalNode)
            assertEquals(literalType, literalNode.getLiteralAttribute())
            if (literalNode.getLiteralAttribute() == CPlusPlusLiteral.stringValueLiteral()) {
                assertEquals(literalValue, literalNode.getLiteralValue())
            } else {
                assertEquals(literalValue as double, literalNode.getLiteralValue() as double, 0.0)
            }
            foundLiteral = true
        }
        assertTrue(foundLiteral)
    }
}
