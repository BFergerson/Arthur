package com.codebrig.arthur.observe.structure.literal

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.LiteralFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.NameFilter
import com.codebrig.arthur.observe.structure.filter.operator.relational.define.InitializeVariableOperatorFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.*

class PythonLiteralTest extends ArthurTest {

    @Test
    void intLiteralTest() {
        assertPythonLiteralPresent("param1", "numberValue", 1)
    }

    @Test
    void stringLiteralTest() {
        assertPythonLiteralPresent("param2", "stringValue", "stringParam2")
    }

    @Test
    void binaryLiteralTest() {
        assertPythonLiteralPresent("param3", "numberValue", 0b01111111100000000000000000000000)
        assertPythonLiteralPresent("param4", "numberValue", -0b01111111100000000000000000000000)
    }

    @Test
    void octalLiteralTest() {
        assertPythonLiteralPresent("param5", "numberValue", "0o10")
        assertPythonLiteralPresent("param6", "numberValue", "-0o10")
    }

    @Test
    void hexadecimalLiteralTest() {
        assertPythonLiteralPresent("param7", "numberValue", 0xFFFFFFFFFFFFFFFF)
    }

    @Test
    void signedEngineeringNotationLiteralTest() {
        assertPythonLiteralPresent("param8", "numberValue", 1.2e-55)
        assertPythonLiteralPresent("param9", "numberValue", -1.2e-55)
        assertPythonLiteralPresent("param10", "numberValue", 0.1E3)
    }

    private static void assertPythonLiteralPresent(String literalName, String literalType, Object literalValue) {
        def file = new File("src/test/resources/same/literals/Literals.py")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        boolean foundLiteral = false
        MultiFilter.matchAll(new NameFilter(literalName), new InitializeVariableOperatorFilter())
                .getFilteredNodes(language, resp.uast).each {
            assertEquals(literalName, it.name)
            def literalNode = new LiteralFilter().getFilteredNodes(it).next()
            assertNotNull(literalNode)
            assertEquals(literalType, literalNode.getLiteralAttribute())
            if (literalNode.getLiteralAttribute() == PythonLiteral.stringValueLiteral()) {
                assertEquals(literalValue, literalNode.getLiteralValue())
            } else if (PythonLiteral.isOctalLiteral(literalValue as String)) {
                assertEquals(PythonLiteral.getOctalValue(literalNode) as double, literalNode.getLiteralValue() as double, 0.0)
            } else {
                assertEquals(literalValue as double, literalNode.getLiteralValue() as double, 0.0)
            }
            foundLiteral = true
        }
        assertTrue(foundLiteral)
    }
}
