package com.codebrig.arthur.observe.structure.filter.operator.misc

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.FunctionFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.NameFilter
import org.junit.Test

import static org.junit.Assert.*

class TernaryOperatorFilterTest extends ArthurTest {

    @Test
    void ternaryOperator_Java() {
        assertTernaryOperatorPresent(new File("src/test/resources/same/operators/Operators.java"),
                "Operators.")
    }

    @Test
    void ternaryOperator_Javascript() {
        assertTernaryOperatorPresent(new File("src/test/resources/same/operators/Operators.js"))
    }

    private static void assertTernaryOperatorPresent(File file) {
        assertTernaryOperatorPresent(file, "")
    }

    private static void assertTernaryOperatorPresent(File file, String qualifiedName) {
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key)

        def foundTernaryOperator = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("ternaryOperator")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals(qualifiedName + "ternaryOperator()", it.name)

            new TernaryOperatorFilter().getFilteredNodes(it).each {
                assertFalse(foundTernaryOperator)
                foundTernaryOperator = true
            }
        }
        assertTrue(foundTernaryOperator)
    }
}