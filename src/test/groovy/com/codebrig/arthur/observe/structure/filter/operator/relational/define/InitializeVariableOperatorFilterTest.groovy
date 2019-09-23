package com.codebrig.arthur.observe.structure.filter.operator.relational.define

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.FunctionFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.NameFilter
import com.codebrig.arthur.observe.structure.filter.TokenFilter
import org.bblfsh.client.v2.BblfshClient
import org.junit.Test

import static org.junit.Assert.*

class InitializeVariableOperatorFilterTest extends ArthurTest {

    @Test
    void initializeVariableOperator_Go() {
        assertInitializeVariableOperatorPresent(new File("src/test/resources/same/operators/Operators.go"))
    }

    @Test
    void initializeVariableOperator_Java() {
        assertInitializeVariableOperatorPresent(new File("src/test/resources/same/operators/Operators.java"),
                "Operators.")
    }

    @Test
    void initializeVariableOperator_Javascript() {
        assertInitializeVariableOperatorPresent(new File("src/test/resources/same/operators/Operators.js"))
    }

    @Test
    void initializeVariableOperator_Php() {
        assertInitializeVariableOperatorPresent(new File("src/test/resources/same/operators/Operators.php"))
    }

    @Test
    void initializeVariableOperator_Python() {
        assertInitializeVariableOperatorPresent(new File("src/test/resources/same/operators/Operators.py"))
    }

    @Test
    void initializeVariableOperator_CSharp() {
        assertInitializeVariableOperatorPresent(new File("src/test/resources/same/operators/Operators.cs"))
    }

    @Test
    void initializeVariableOperator_CPlusPlus() {
        assertInitializeVariableOperatorPresent(new File("src/test/resources/same/operators/Operators.cpp"))
    }

    private static void assertInitializeVariableOperatorPresent(File file) {
        assertInitializeVariableOperatorPresent(file, "")
    }

    private static void assertInitializeVariableOperatorPresent(File file, String qualifiedName) {
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.babelfishName)
        def rootNode = new BblfshClient.UastMethods(resp.uast()).decode().root().load()

        def foundInitializeVariableOperator = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter(qualifiedName + "initializeVariableOperator()")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, rootNode).each {
            assertEquals(qualifiedName + "initializeVariableOperator()", it.name)

            new InitializeVariableOperatorFilter().getFilteredNodes(it).each {
                assertFalse(foundInitializeVariableOperator)
                assertNotNull(new TokenFilter("x").getFilteredNodes(it).next())
                foundInitializeVariableOperator = true
            }
        }
        assertTrue(foundInitializeVariableOperator)
    }
}
