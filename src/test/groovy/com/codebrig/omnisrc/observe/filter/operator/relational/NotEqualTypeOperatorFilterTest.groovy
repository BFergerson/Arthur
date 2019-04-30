package com.codebrig.omnisrc.observe.filter.operator.relational

import com.codebrig.omnisrc.OmniSRCTest
import com.codebrig.omnisrc.SourceLanguage
import com.codebrig.omnisrc.observe.filter.FunctionFilter
import com.codebrig.omnisrc.observe.filter.MultiFilter
import com.codebrig.omnisrc.observe.filter.NameFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.*

class NotEqualTypeOperatorFilterTest extends OmniSRCTest {

    @Test
    void notEqualTypeOperator_Javascript() {
        assertNotEqualTypeOperatorPresent(new File("src/test/resources/same/operators/Operators.js"))
    }

    @Test
    void notEqualTypeOperator_Php() {
        assertNotEqualTypeOperatorPresent(new File("src/test/resources/same/operators/Operators.php"))
    }

    private static void assertNotEqualTypeOperatorPresent(File file) {
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundNotEqualTypeOperator = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("notEqualTypeOperator")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("notEqualTypeOperator()", it.name)

            new NotEqualTypeOperatorFilter().getFilteredNodes(it).each {
                assertFalse(foundNotEqualTypeOperator)
                if (!it.token.isEmpty()) assertEquals("!==", it.token)
                foundNotEqualTypeOperator = true
            }
        }
        assertTrue(foundNotEqualTypeOperator)
    }
}