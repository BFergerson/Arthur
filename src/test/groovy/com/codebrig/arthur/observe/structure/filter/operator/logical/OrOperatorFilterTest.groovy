package com.codebrig.arthur.observe.structure.filter.operator.logical

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.FunctionFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.NameFilter
import org.bblfsh.client.v2.BblfshClient
import org.junit.Test

import static org.junit.Assert.*

class OrOperatorFilterTest extends ArthurTest {

    @Test
    void orOperator_Go() {
        assertOrOperatorPresent(new File("src/test/resources/same/operators/Operators.go"),
                "||")
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
                "or")
    }

    @Test
    void orOperator_CSharp() {
        assertOrOperatorPresent(new File("src/test/resources/same/operators/Operators.cs"),
                "||")
    }

    @Test
    void orOperator_CPlusPlus() {
        assertOrOperatorPresent(new File("src/test/resources/same/operators/Operators.cpp"),
                "||")
    }

    /**
     * Babelfish treats both Ruby "||" and "or" operators as "or" token
     */
    @Test
    void orOperator_Ruby() {
        assertOrOperatorPresent(new File("src/test/resources/same/operators/Operators.rb"),
                "or", "", "orOperator1()")
        assertOrOperatorPresent(new File("src/test/resources/same/operators/Operators.rb"),
                "or", "", "orOperator2()")
    }

    @Test
    void orOperator_Bash() {
        assertOrOperatorPresent(new File("src/test/resources/same/operators/Operators.sh"),
                "||", "")
    }

    private static void assertOrOperatorPresent(File file, String orToken) {
        assertOrOperatorPresent(file, orToken, "", "orOperator()")
    }

    private static void assertOrOperatorPresent(File file, String orToken, String qualifiedName) {
        assertOrOperatorPresent(file, orToken, qualifiedName, "orOperator()")
    }

    private static void assertOrOperatorPresent(File file, String orToken, String qualifiedName, String functionName) {
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.babelfishName)
        def rootNode = new BblfshClient.UastMethods(resp.uast()).decode().root().load()

        def foundOrOperator = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter(qualifiedName + functionName)
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, rootNode).each {
            assertEquals(qualifiedName + functionName, it.name)

            new OrOperatorFilter().getFilteredNodes(it).each {
                assertFalse(foundOrOperator)
                assertEquals(orToken, it.token)
                foundOrOperator = true
            }
        }
        assertTrue(foundOrOperator)
    }
}