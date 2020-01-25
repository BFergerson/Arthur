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

class BashLiteralTest extends ArthurTest {

    @Test
    void intLiteralTest() {
        assertBashLiteralPresent("param1", "numberValue", 100)
    }

    @Test
    void stringLiteralTest() {
        assertBashLiteralPresent("param2", "stringValue", "stringParam2")
        assertBashLiteralPresent("param3", "stringValue", "9.234")
    }

    @Test
    void binaryLiteralTest() {
        assertBashLiteralPresent("param4", "numberValue", 0b1010000101000101101000010100010110100001010001011010000101000101L)
        assertBashLiteralPresent("param5", "numberValue", 0b00101010)
        assertBashLiteralPresent("param6", "numberValue", -0b00101010)
    }

    @Test
    void octalLiteralTest() {
        assertBashLiteralPresent("param7", "numberValue", 0123546263753256452432L)
        assertBashLiteralPresent("param8", "numberValue", -0123546263753256452432L)
    }

    @Test
    void hexadecimalLiteralTest() {
        assertBashLiteralPresent("param9", "numberValue", 0xFFFFFFFFFFFF)
        assertBashLiteralPresent("param10", "numberValue", 0xff1a618b7f65ea12L)
        assertBashLiteralPresent("param11", "numberValue", -0x1000000)
        assertBashLiteralPresent("param12", "numberValue", -0xc4ceb9fe1a85ec53L)
    }

    @Test
    void otherBaseLiteralTest() {
        assertBashLiteralPresent("param13", "numberValue", "4#123123")
        assertBashLiteralPresent("param14", "numberValue", "-4#123123")
        assertBashLiteralPresent("param15", "numberValue", "5#12341234")
        assertBashLiteralPresent("param16", "numberValue", "-5#12341234")
    }

    private static void assertBashLiteralPresent(String literalName, String literalType, Object literalValue) {
        def file = new File("src/test/resources/same/literals/Literals.sh")
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
            if (literalNode.getLiteralAttribute() == BashLiteral.stringValueLiteral()) {
                assertEquals(literalValue, literalNode.getLiteralValue())
            } else {
                String[] array = BashLiteral.parseLiteralToken(literalValue as String)
                if (array) {
                    def value = new BigInteger(array[1], Integer.parseInt(array[0])).longValue()
                    assertEquals(value as double, literalNode.getLiteralValue() as double, 0.0)
                } else {
                    assertEquals(literalValue as double, literalNode.getLiteralValue() as double, 0.0)
                }
            }
            foundLiteral = true
        }
        assertTrue(foundLiteral)
    }
}
