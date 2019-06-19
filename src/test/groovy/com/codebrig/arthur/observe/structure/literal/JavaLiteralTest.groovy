package com.codebrig.arthur.observe.structure.literal

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.LiteralFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

class JavaLiteralTest extends ArthurTest {

    private static final Logger log = LoggerFactory.getLogger(this.name)

    @Test
    void intTest() {
        assertJavaLiteralPresent("param1", "numberValue")
    }

    @Test
    void doubleTest() {
        assertJavaLiteralPresent("param2", "doubleValue")
    }

    private static void assertJavaLiteralPresent(String literalName, String literalType) {
        def file = new File("src/test/resources/same/literal/Literals.java")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def literalFilter = new LiteralFilter(literalName)
        boolean foundLiteral = false
        MultiFilter.matchAll(literalFilter).getFilteredNodes(language, resp.uast).each {
            log.info ">>>>> it.name = ${it.name}"
            assertEquals(literalName + ",type=" + literalType, it.name)
            foundLiteral = true
        }
        assertTrue(foundLiteral)
    }
}
