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
        assertCPlusPlusLiteralPresent("param13", "numberValue", 100000)
        assertCPlusPlusLiteralPresent("param14", "numberValue", -4000)
        assertCPlusPlusLiteralPresent("param15", "numberValue", 12000000)
    }

    @Test
    void stringLiteralTest() {
        assertCPlusPlusLiteralPresent("param16", "stringValue", StringEscapeUtils.escapeJava("\"stringParam16\""))
    }

    @Test
    void doubleLiteralTest() {
        assertCPlusPlusLiteralPresent("param17", "doubleValue", 9.234)
        assertCPlusPlusLiteralPresent("param18", "doubleValue", 100.72)
        assertCPlusPlusLiteralPresent("param19", "doubleValue", -100.72)
        assertCPlusPlusLiteralPresent("param20", "doubleValue", 100.72)
        assertCPlusPlusLiteralPresent("param21", "doubleValue", 1000.72)
    }

    @Test
    void octalLiteralTest() {
        assertCPlusPlusLiteralPresent("param22", "numberValue", 0123567)
        assertCPlusPlusLiteralPresent("param23", "numberValue", 0123546263753256452432L)
        assertCPlusPlusLiteralPresent("param24", "numberValue", -0123546263753256452432L)
    }

    @Test
    void hexadecimalLiteralTest() {
        assertCPlusPlusLiteralPresent("param25", "numberValue", 0xFFFFFFFFFFFFL)
        assertCPlusPlusLiteralPresent("param26", "numberValue", 0xff1a618b7f65ea12L)
        assertCPlusPlusLiteralPresent("param27", "numberValue", -0x1000000)
        assertCPlusPlusLiteralPresent("param28", "numberValue", -0xc4ceb9fe1a85ec53L)
        assertCPlusPlusLiteralPresent("param29", "doubleValue", Double.parseDouble('0x1.0p31'))
        assertCPlusPlusLiteralPresent("param30", "doubleValue", Float.parseFloat('0x1.0p2f'))
        assertCPlusPlusLiteralPresent("param31", "doubleValue", Float.parseFloat('-0x1.0p2f'))
        assertCPlusPlusLiteralPresent("param32", "doubleValue", Double.parseDouble('0xa.bp10d'))
    }

    @Test
    void signedEngineeringNotationLiteralTest() {
        assertCPlusPlusLiteralPresent("param33", "doubleValue", -1.2e55)
        assertCPlusPlusLiteralPresent("param34", "doubleValue", 1.2e-55)
        assertCPlusPlusLiteralPresent("param35", "doubleValue", -1.2e-55)
        assertCPlusPlusLiteralPresent("param36", "doubleValue", 1000.2e-55)
        assertCPlusPlusLiteralPresent("param37", "doubleValue", -1000.2e55)
        assertCPlusPlusLiteralPresent("param38", "doubleValue", 2000.123e55)
    }

    private static void assertCPlusPlusLiteralPresent(String literalName, String literalType, Object literalValue) {
        def file = new File("src/test/resources/same/literals/Literals.cpp")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.babelfishName, Encoding.UTF8$.MODULE$)

        boolean foundLiteral = false
        MultiFilter.matchAll(new NameFilter(literalName), new InitializeVariableOperatorFilter())
                .getFilteredNodesIncludingCurrent(language, resp.uast).each {
            assertEquals(literalName, it.name)
            def literalNode = new LiteralFilter().getFilteredNodesIncludingCurrent(it).next()
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
