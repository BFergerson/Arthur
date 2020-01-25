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

class RubyLiteralTest extends ArthurTest {

    @Test
    void intLiteralTest() {
        assertRubyLiteralPresent("param1", "numberValue", 1)
        assertRubyLiteralPresent("param5", "numberValue", 1000)
        assertRubyLiteralPresent("param6", "numberValue", 10000)
        assertRubyLiteralPresent("param7", "numberValue", -10000)
    }

    @Test
    void stringLiteralTest() {
        assertRubyLiteralPresent("param2", "stringValue", "stringParam2")
    }

    @Test
    void doubleLiteralTest() {
        assertRubyLiteralPresent("param3", "doubleValue", 100.1)
        assertRubyLiteralPresent("param8", "doubleValue", -10000.1)
    }

    @Test
    void binaryLiteralTest() {
        assertRubyLiteralPresent("param9", "numberValue", 0b01111111100000000000000000000000)
        assertRubyLiteralPresent("param10", "numberValue", 0B00000000011111111111111111111111)
    }

    @Test
    void octalLiteralTest() {
        assertRubyLiteralPresent("param11", "numberValue", 0777)
        assertRubyLiteralPresent("param12", "numberValue", "0o10")
        assertRubyLiteralPresent("param13", "numberValue", "-0o10")
    }

    @Test
    void hexadecimalLiteralTest() {
        assertRubyLiteralPresent("param14", "numberValue", 0x123456789ABCDEF)
        assertRubyLiteralPresent("param15", "doubleValue", 0xFFFFFFFFFFFFFFFF)
    }

    @Test
    void signedEngineeringNotationLiteralTest() {
        assertRubyLiteralPresent("param16", "doubleValue", -1.2e55)
        assertRubyLiteralPresent("param17", "doubleValue", 1.2e-55)
        assertRubyLiteralPresent("param18", "doubleValue", -1.2e-55)
        assertRubyLiteralPresent("param19", "doubleValue", 1000.2e-55)
        assertRubyLiteralPresent("param20", "doubleValue", -1000.2e55)
    }

    private static void assertRubyLiteralPresent(String literalName, String literalType, Object literalValue) {
        def file = new File("src/test/resources/same/literals/Literals.rb")
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
            if (literalNode.getLiteralAttribute() == RubyLiteral.stringValueLiteral()) {
                assertEquals(literalValue, literalNode.getLiteralValue())
            } else if (RubyLiteral.isOctalLiteral(literalValue as String)) {
                assertEquals(RubyLiteral.getOctalValue(literalNode) as double, literalNode.getLiteralValue() as double, 0.0)
            } else {
                assertEquals(literalValue as double, literalNode.getLiteralValue() as double, 0.0)
            }
            foundLiteral = true
        }
        assertTrue(foundLiteral)
    }
}
