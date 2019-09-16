package com.codebrig.arthur.observe.structure.filter.operator.relational.compare

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
    void isNotEqualOperator_CSharp() {
        assertIsNotEqualOperatorPresent(new File("src/test/resources/same/operators/Operators.cs"))
    }

    @Test
    void isNotEqualOperator_Bash() {
        assertIsNotEqualOperatorPresent(new File("src/test/resources/same/operators/Operators.sh"),
                "", "isNotEqualOperator1()", "!=")
        assertIsNotEqualOperatorPresent(new File("src/test/resources/same/operators/Operators.sh"),
                "", "isNotEqualOperator2()", "-ne")
    }

    @Test
    void alternateNotEqualOperator_Python() {
        def file = new File("src/test/resources/same/operators/Operators.py")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundAlternateNotEqualOperator = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("alternateIsNotEqualOperator()")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("alternateIsNotEqualOperator()", it.name)

            new IsNotEqualOperatorFilter().getFilteredNodes(it).each {
                assertFalse(foundAlternateNotEqualOperator)
                assertEquals("!=", it.token)
                foundAlternateNotEqualOperator = true
            }
        }
        assertTrue(foundAlternateNotEqualOperator)
    }

    private static void assertIsNotEqualOperatorPresent(File file) {
        assertIsNotEqualOperatorPresent(file, "", "isNotEqualOperator()", "!=")
    }

    private static void assertIsNotEqualOperatorPresent(File file, String qualifiedName) {
        assertIsNotEqualOperatorPresent(file, qualifiedName, "isNotEqualOperator()", "!=")
    }

    private static void assertIsNotEqualOperatorPresent(File file, String qualifiedName, String functionName, String operator) {
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundNotEqualOperator = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter(qualifiedName + functionName)
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals(qualifiedName + functionName, it.name)

            new IsNotEqualOperatorFilter().getFilteredNodes(it).each {
                assertFalse(foundNotEqualOperator)
                if (!IsNotEqualOperatorFilter.getIsNotEqualToken(it).isEmpty()) assertEquals(operator, IsNotEqualOperatorFilter.getIsNotEqualToken(it))
                foundNotEqualOperator = true
            }
        }
        assertTrue(foundNotEqualOperator)
    }
}