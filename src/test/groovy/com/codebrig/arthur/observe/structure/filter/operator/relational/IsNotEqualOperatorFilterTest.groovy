package com.codebrig.arthur.observe.structure.filter.operator.relational

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.FunctionFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.NameFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.*

class IsNotEqualOperatorFilterTest extends ArthurTest {

    @Test
    void isNotEqualOperator_Go() {
        assertIsNotEqualOperatorPresent(new File("src/test/resources/same/operators/Operators.go"))
    }

    @Test
    void isNotEqualOperator_Java() {
        assertIsNotEqualOperatorPresent(new File("src/test/resources/same/operators/Operators.java"),
                "Operators.")
    }

    @Test
    void isNotEqualOperator_Javascript() {
        assertIsNotEqualOperatorPresent(new File("src/test/resources/same/operators/Operators.js"))
    }

    @Test
    void isNotEqualOperator_Php() {
        assertIsNotEqualOperatorPresent(new File("src/test/resources/same/operators/Operators.php"))
    }

    @Test
    void isNotEqualOperator_Python() {
        assertIsNotEqualOperatorPresent(new File("src/test/resources/same/operators/Operators.py"))
    }

    @Test
    void alternateNotEqualOperator_Python() {
        def file = new File("src/test/resources/same/operators/Operators.py")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundAlternateNotEqualOperator = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("alternateNotEqualOperator")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("alternateNotEqualOperator()", it.name)

            new IsNotEqualOperatorFilter().getFilteredNodes(it).each {
                assertFalse(foundAlternateNotEqualOperator)
                assertEquals("!=", it.token)
                foundAlternateNotEqualOperator = true
            }
        }
        assertTrue(foundAlternateNotEqualOperator)
    }

    private static void assertIsNotEqualOperatorPresent(File file) {
        assertIsNotEqualOperatorPresent(file, "")
    }

    private static void assertIsNotEqualOperatorPresent(File file, String qualifiedName) {
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundNotEqualOperator = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("notEqualOperator")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals(qualifiedName + "notEqualOperator()", it.name)

            new IsNotEqualOperatorFilter().getFilteredNodes(it).each {
                assertFalse(foundNotEqualOperator)
                if (!it.token.isEmpty()) assertEquals("!=", it.token)
                foundNotEqualOperator = true
            }
        }
        assertTrue(foundNotEqualOperator)
    }
}