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

    @Test
    void isEqualOperator_Bash() {
        assertIsEqualOperatorPresent(new File("src/test/resources/same/operators/Operators.sh"),
                "", "isEqualOperator1()", "==")
        assertIsEqualOperatorPresent(new File("src/test/resources/same/operators/Operators.sh"),
                "", "isEqualOperator2()", "-eq")
    }

    private static void assertIsEqualOperatorPresent(File file) {
        assertIsEqualOperatorPresent(file, "", "isEqualOperator()", "==")
    }

    private static void assertIsEqualOperatorPresent(File file, String qualifiedName) {
        assertIsEqualOperatorPresent(file, qualifiedName, "isEqualOperator()", "==")
    }

    private static void assertIsEqualOperatorPresent(File file, String qualifiedName, String functionName, String operator) {
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundEqualOperator = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter(qualifiedName + functionName)
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals(qualifiedName + functionName, it.name)

            new IsEqualOperatorFilter().getFilteredNodes(it).each {
                assertFalse(foundEqualOperator)
                if (!IsEqualOperatorFilter.getIsEqualToken(it).isEmpty()) assertEquals(operator, IsEqualOperatorFilter.getIsEqualToken(it))
                foundEqualOperator = true
            }
        }
        assertTrue(foundEqualOperator)
    }
}