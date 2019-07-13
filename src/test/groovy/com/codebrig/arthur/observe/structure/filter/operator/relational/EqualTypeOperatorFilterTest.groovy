package com.codebrig.arthur.observe.structure.filter.operator.relational

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.FunctionFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.NameFilter
import org.junit.Test

import static org.junit.Assert.*

class EqualTypeOperatorFilterTest extends ArthurTest {

    @Test
    void equalTypeOperator_Javascript() {
        assertEqualTypeOperatorPresent(new File("src/test/resources/same/operators/Operators.js"))
    }

    @Test
    void equalTypeOperator_Php() {
        assertEqualTypeOperatorPresent(new File("src/test/resources/same/operators/Operators.php"))
    }

    private static void assertEqualTypeOperatorPresent(File file) {
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key)

        def foundEqualTypeOperator = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("equalTypeOperator")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("equalTypeOperator()", it.name)

            new EqualTypeOperatorFilter().getFilteredNodes(it).each {
                assertFalse(foundEqualTypeOperator)
                if (!it.token.isEmpty()) assertEquals("===", it.token)
                foundEqualTypeOperator = true
            }
        }
        assertTrue(foundEqualTypeOperator)
    }
}