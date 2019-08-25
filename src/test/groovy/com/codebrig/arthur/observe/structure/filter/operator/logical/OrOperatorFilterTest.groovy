package com.codebrig.arthur.observe.structure.filter.operator.logical

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.FunctionFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.NameFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.*

class OrOperatorFilterTest extends ArthurTest {

    @Test
    void orOperator_Go() {
        assertOrOperatorPresent(new File("src/test/resources/same/operators/Operators.go"),
                "||", "")
    }

    @Test
    void orOperator_Java() {
        assertOrOperatorPresent(new File("src/test/resources/same/operators/Operators.java"),
                "||", "Operators.")
    }

    @Test
    void orOperator_Javascript() {
        assertOrOperatorPresent(new File("src/test/resources/same/operators/Operators.js"),
                "||")
    }

    @Test
    void orOperator_Python() {
        assertOrOperatorPresent(new File("src/test/resources/same/operators/Operators.py"),
                "or", "")
    }

    @Test
    void orOperator_CSharp() {
        assertOrOperatorPresent(new File("src/test/resources/same/operators/Operators.cs"),
                "||", "")
    }

    @Test
    void orOperator_Bash() {
        assertOrOperatorPresent(new File("src/test/resources/same/operators/operators.sh"),
                "||", "")
    }

    private static void assertOrOperatorPresent(File file, String orToken) {
        assertOrOperatorPresent(file, orToken, "")
    }

    private static void assertOrOperatorPresent(File file, String orToken, String qualifiedName) {
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundOrOperator = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter(qualifiedName + "orOperator()")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals(qualifiedName + "orOperator()", it.name)

            new OrOperatorFilter().getFilteredNodes(it).each {
                assertFalse(foundOrOperator)
                assertEquals(orToken, it.token)
                foundOrOperator = true
            }
        }
        assertTrue(foundOrOperator)
    }
}