package com.codebrig.arthur.observe.structure.filter.operator.relational.compare

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.FunctionFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.NameFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.*

class IsNotEqualTypeOperatorFilterTest extends ArthurTest {

    @Test
    void isNotEqualTypeOperator_Javascript() {
        assertIsNotEqualTypeOperatorPresent(new File("src/test/resources/same/operators/Operators.js"))
    }

    @Test
    void isNotEqualTypeOperator_Php() {
        assertIsNotEqualTypeOperatorPresent(new File("src/test/resources/same/operators/Operators.php"))
    }

    private static void assertIsNotEqualTypeOperatorPresent(File file) {
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.babelfishName, Encoding.UTF8$.MODULE$)

        def foundNotEqualTypeOperator = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("isNotEqualTypeOperator()")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("isNotEqualTypeOperator()", it.name)

            new IsNotEqualTypeOperatorFilter().getFilteredNodes(it).each {
                assertFalse(foundNotEqualTypeOperator)
                if (!it.token.isEmpty()) assertEquals("!==", it.token)
                foundNotEqualTypeOperator = true
            }
        }
        assertTrue(foundNotEqualTypeOperator)
    }
}