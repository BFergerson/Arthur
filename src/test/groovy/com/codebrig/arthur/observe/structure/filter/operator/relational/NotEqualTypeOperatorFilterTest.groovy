package com.codebrig.arthur.observe.structure.filter.operator.relational

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.FunctionFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.NameFilter
import org.junit.Test

import static org.junit.Assert.*

class NotEqualTypeOperatorFilterTest extends ArthurTest {

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
        def resp = client.parse(file.name, file.text, language.key)

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