package com.codebrig.arthur.observe.structure.filter.operator.relational.compare

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.FunctionFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.NameFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.*

class IsEqualOperatorFilterTest extends ArthurTest {

    @Test
    void isEqualOperator_Go() {
        assertIsEqualOperatorPresent(new File("src/test/resources/same/operators/Operators.go"))
    }

    @Test
    void isEqualOperator_Java() {
        assertIsEqualOperatorPresent(new File("src/test/resources/same/operators/Operators.java"),
                "Operators.")
    }

    @Test
    void isEqualOperator_Javascript() {
        assertIsEqualOperatorPresent(new File("src/test/resources/same/operators/Operators.js"))
    }

    @Test
    void isEqualOperator_Php() {
        assertIsEqualOperatorPresent(new File("src/test/resources/same/operators/Operators.php"))
    }

    @Test
    void isEqualOperator_Python() {
        assertIsEqualOperatorPresent(new File("src/test/resources/same/operators/Operators.py"))
    }

    @Test
    void isEqualOperator_CSharp() {
        assertIsEqualOperatorPresent(new File("src/test/resources/same/operators/Operators.cs"))
    }

    private static void assertIsEqualOperatorPresent(File file) {
        assertIsEqualOperatorPresent(file, "")
    }

    private static void assertIsEqualOperatorPresent(File file, String qualifiedName) {
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundEqualOperator = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter(qualifiedName + "isEqualOperator()")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals(qualifiedName + "isEqualOperator()", it.name)

            new IsEqualOperatorFilter().getFilteredNodes(it).each {
                assertFalse(foundEqualOperator)
                if (!it.token.isEmpty()) assertEquals("==", it.token)
                foundEqualOperator = true
            }
        }
        assertTrue(foundEqualOperator)
    }
}