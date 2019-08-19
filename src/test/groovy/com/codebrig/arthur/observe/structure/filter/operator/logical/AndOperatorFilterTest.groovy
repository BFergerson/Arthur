package com.codebrig.arthur.observe.structure.filter.operator.logical

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.FunctionFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.NameFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.*

class AndOperatorFilterTest extends ArthurTest {

    @Test
    void andOperator_Go() {
        assertAndOperatorPresent(new File("src/test/resources/same/operators/Operators.go"),
                "&&")
    }

    @Test
    void andOperator_Java() {
        assertAndOperatorPresent(new File("src/test/resources/same/operators/Operators.java"),
                "&&", "Operators.")
    }

    @Test
    void andOperator_Javascript() {
        assertAndOperatorPresent(new File("src/test/resources/same/operators/Operators.js"),
                "&&")
    }

    @Test
    void andOperator_Python() {
        assertAndOperatorPresent(new File("src/test/resources/same/operators/Operators.py"),
                "and")
    }

    @Test
    void andOperator_CSharp() {
        assertAndOperatorPresent(new File("src/test/resources/same/operators/Operators.cs"),
                "&&")
    }

    /**
     * Babelfish treats both Ruby "&&" and "and" operators as "and" token
     */
    @Test
    void andOperator_Ruby() {
        assertAndOperatorPresent(new File("src/test/resources/same/operators/Operators.rb"),
                "and", "", "andOperator1()")
        assertAndOperatorPresent(new File("src/test/resources/same/operators/Operators.rb"),
                "and", "", "andOperator2()")
    }

    private static void assertAndOperatorPresent(File file, String andToken) {
        assertAndOperatorPresent(file, andToken, "", "andOperator()")
    }

    private static void assertAndOperatorPresent(File file, String andToken, String qualifiedName) {
        assertAndOperatorPresent(file, andToken, qualifiedName, "andOperator()")
    }

    private static void assertAndOperatorPresent(File file, String andToken, String qualifiedName, String functionName) {
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundAndOperator = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter(qualifiedName + functionName)
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals(qualifiedName + functionName, it.name)

            new AndOperatorFilter().getFilteredNodes(it).each {
                assertFalse(foundAndOperator)
                assertEquals(andToken, it.token)
                foundAndOperator = true
            }
        }
        assertTrue(foundAndOperator)
    }
}