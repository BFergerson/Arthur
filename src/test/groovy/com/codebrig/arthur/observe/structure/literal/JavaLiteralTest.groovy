package com.codebrig.arthur.observe.structure.literal

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.LiteralFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.NameFilter
import com.codebrig.arthur.observe.structure.filter.operator.relational.define.InitializeVariableOperatorFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import groovy.util.logging.Slf4j
import org.apache.commons.lang.StringEscapeUtils
import org.junit.Test

import static org.junit.Assert.*

@Slf4j
class JavaLiteralTest extends ArthurTest {

    @Test
    void intTest() {
        assertJavaLiteralPresent("param1", "numberValue", 100)
    }

    @Test
    void doubleTest() {
        assertJavaLiteralPresent("param2", "doubleValue", 200.1)
    }

    @Test
    void stringTest() {
        assertJavaLiteralPresent("param3", "stringValue", StringEscapeUtils.escapeJava("\"stringParam3\""))
    }

    @Test
    void signedNegativeEngineeringNotationTest() {
        assertJavaLiteralPresent("param4", "doubleValue", -1.2e55)
    }

    private static void assertJavaLiteralPresent(String literalName, String literalType, Object literalValue) {
        def file = new File("src/test/resources/same/literals/Literals.java")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        boolean foundLiteral = false
        MultiFilter.matchAll(new NameFilter(literalName), new InitializeVariableOperatorFilter())
                .getFilteredNodes(language, resp.uast).each {
            assertEquals(literalName, it.name)
            def literalNode = new LiteralFilter().getFilteredNodes(it).next()
            assertNotNull(literalNode)
            assertEquals(literalType, literalNode.getLiteralAttribute())
            if (literalNode.getLiteralAttribute() == JavaLiteral.stringValueLiteral()) {
                assertEquals(literalValue, literalNode.getLiteralValue())
            } else {
                assertEquals(literalValue as double, literalNode.getLiteralValue() as double, 0.0)
            }
            foundLiteral = true
        }
        assertTrue(foundLiteral)
    }
}
