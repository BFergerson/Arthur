package com.codebrig.arthur.observe.structure.literal

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.LiteralFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.NameFilter
import com.codebrig.arthur.observe.structure.filter.operator.relational.define.InitializeVariableOperatorFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.apache.commons.text.StringEscapeUtils
import org.junit.Test

import static org.junit.Assert.*

class CSharpLiteralTest extends ArthurTest {

    @Test
    void intLiteralTest() {
        assertCSharpLiteralPresent("param1", "numberValue", 100)
        assertCSharpLiteralPresent("param2", "numberValue", "1000")
        assertCSharpLiteralPresent("param3", "numberValue", "-10000")
        assertCSharpLiteralPresent("param4", "numberValue", 1000)
        assertCSharpLiteralPresent("param5", "numberValue", 52)
        assertCSharpLiteralPresent("param6", "numberValue", 52)
    }

    @Test
    void stringLiteralTest() {
        assertCSharpLiteralPresent("param7", "stringValue", StringEscapeUtils.escapeJava("\"stringParam7\""))
    }

    @Test
    void longLiteralTest() {
        assertCSharpLiteralPresent("param8", "numberValue", 1000)
        assertCSharpLiteralPresent("param9", "numberValue", "18446744073709551615")
        assertCSharpLiteralPresent("param10", "numberValue", "-18446744073709551615")
    }

    @Test
    void doubleLiteralTest() {
        assertCSharpLiteralPresent("param11", "doubleValue", 9.234)
        assertCSharpLiteralPresent("param12", "doubleValue", 100.72)
        assertCSharpLiteralPresent("param13", "doubleValue", 100.72)
        assertCSharpLiteralPresent("param14", "doubleValue", 1.44)
        assertCSharpLiteralPresent("param15", "doubleValue", -1.44)
    }

    @Test
    void binaryLiteralTest() {
        assertCSharpLiteralPresent("param16", "numberValue", 0b1010000101000101101000010100010110100001010001011010000101000101L)
        assertCSharpLiteralPresent("param17", "numberValue", 0b00101010)
        assertCSharpLiteralPresent("param18", "numberValue", -0b00101010)
    }

    @Test
    void octalLiteralTest() {
        assertCSharpLiteralPresent("param19", "numberValue", 0123546263753256452432L)
        assertCSharpLiteralPresent("param20", "numberValue", -0123546263753256452432L)
    }

    @Test
    void hexadecimalLiteralTest() {
        assertCSharpLiteralPresent("param21", "numberValue", 0xFFFFFFFFFFFFL)
        assertCSharpLiteralPresent("param22", "numberValue", 0xff1a618b7f65ea12L)
        assertCSharpLiteralPresent("param23", "numberValue", -0x1000000)
        assertCSharpLiteralPresent("param24", "numberValue", -0xc4ceb9fe1a85ec53L)
    }

    @Test
    void signedEngineeringNotationLiteralTest() {
        assertCSharpLiteralPresent("param25", "doubleValue", -1.2e55)
        assertCSharpLiteralPresent("param26", "doubleValue", 1.2e-55)
        assertCSharpLiteralPresent("param27", "doubleValue", -1.2e-55)
        assertCSharpLiteralPresent("param28", "doubleValue", 1000.2e-55)
        assertCSharpLiteralPresent("param29", "doubleValue", -1000.2e55)
    }

    private static void assertCSharpLiteralPresent(String literalName, String literalType, Object literalValue) {
        def file = new File("src/test/resources/same/literals/Literals.cs")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.babelfishName, Encoding.UTF8$.MODULE$)

        boolean foundLiteral = false
        MultiFilter.matchAll(new NameFilter(literalName), new InitializeVariableOperatorFilter())
                .getFilteredNodes(language, resp.uast).each {
            assertEquals(literalName, it.name)
            def literalNode = new LiteralFilter().getFilteredNodes(it).next()
            assertNotNull(literalNode)
            assertEquals(literalType, literalNode.getLiteralAttribute())
            if (literalNode.getLiteralAttribute() == CSharpLiteral.stringValueLiteral()) {
                assertEquals(literalValue, literalNode.getLiteralValue())
            } else {
                assertEquals(literalValue as double, literalNode.getLiteralValue() as double, 0.0)
            }
            foundLiteral = true
        }
        assertTrue(foundLiteral)
    }
}
