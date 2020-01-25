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

class JavaLiteralTest extends ArthurTest {

    @Test
    void intLiteralTest() {
        assertJavaLiteralPresent("param1", "numberValue", 100)
        assertJavaLiteralPresent("param10", "numberValue", 10_000)
        assertJavaLiteralPresent("param12", "numberValue", 5_2)
        assertJavaLiteralPresent("param13", "numberValue", 5_______2)
    }

    @Test
    void doubleLiteralTest() {
        assertJavaLiteralPresent("param2", "doubleValue", 200.1)
        assertJavaLiteralPresent("param5", "doubleValue", 9.234d)
    }

    @Test
    void longLiteralTest() {
        assertJavaLiteralPresent("param17", "numberValue", 1234_5678_9012_3456L)
    }

    @Test
    void stringLiteralTest() {
        assertJavaLiteralPresent("param3", "stringValue", StringEscapeUtils.escapeJava("\"stringParam3\""))
    }

    @Test
    void signedEngineeringNotationLiteralTest() {
        assertJavaLiteralPresent("param4", "doubleValue", -1.2e55)
        assertJavaLiteralPresent("param15", "doubleValue", 1.2e-55)
        assertJavaLiteralPresent("param16", "doubleValue", -1.2e-55)
        assertJavaLiteralPresent("param24", "doubleValue", 1000.2e-55)
        assertJavaLiteralPresent("param25", "doubleValue", -1000.2e55)
    }

    @Test
    void hexadecimalLiteralTest() {
        assertJavaLiteralPresent("param6", "numberValue", 0xFFFFFFFFFFFFL)
        assertJavaLiteralPresent("param7", "numberValue", 0xff1a618b7f65ea12L)
        assertJavaLiteralPresent("param8", "numberValue", -0xc4ceb9fe1a85ec53L)
        assertJavaLiteralPresent("param9", "doubleValue", Double.parseDouble('0x1.0p31'))
        assertJavaLiteralPresent("param11", "doubleValue", Double.parseDouble('0xAAAAAAAAAAAAAAAAAAp0d'))
        assertJavaLiteralPresent("param14", "numberValue", 0x52)
        assertJavaLiteralPresent("param22", "doubleValue", Float.parseFloat('0x1.0p2f'))
        assertJavaLiteralPresent("param23", "doubleValue", Float.parseFloat('-0x1.0p2f'))
    }

    @Test
    void octalLiteralTest() {
        assertJavaLiteralPresent("param18", "numberValue", 0757)
        assertJavaLiteralPresent("param19", "numberValue", 0123546263753256452432L)
    }

    @Test
    void binaryLiteralTest() {
        assertJavaLiteralPresent("param20", "numberValue", 0b101101)
        assertJavaLiteralPresent("param21", "numberValue", 0b1010000101000101101000010100010110100001010001011010000101000101L)
    }

    private static void assertJavaLiteralPresent(String literalName, String literalType, Object literalValue) {
        def file = new File("src/test/resources/same/literals/Literals.java")
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
