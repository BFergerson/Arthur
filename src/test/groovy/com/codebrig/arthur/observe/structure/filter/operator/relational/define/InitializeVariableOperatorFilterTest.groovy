package com.codebrig.arthur.observe.structure.filter.operator.relational.define

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.FunctionFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.NameFilter
import com.codebrig.arthur.observe.structure.filter.TokenFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.*

class InitializeVariableOperatorFilterTest extends ArthurTest {

    @Test
    void declareVariableOperator_Go() {
        assertDeclareVariableOperatorPresent(new File("src/test/resources/same/operators/Operators.go"))
    }

    @Test
    void declareVariableOperator_Java() {
        assertDeclareVariableOperatorPresent(new File("src/test/resources/same/operators/Operators.java"),
                "Operators.")
    }

    @Test
    void declareVariableOperator_Javascript() {
        assertDeclareVariableOperatorPresent(new File("src/test/resources/same/operators/Operators.js"))
    }

    @Test
    void declareVariableOperator_Php() {
        assertDeclareVariableOperatorPresent(new File("src/test/resources/same/operators/Operators.php"))
    }

    @Test
    void declareVariableOperator_Python() {
        assertDeclareVariableOperatorPresent(new File("src/test/resources/same/operators/Operators.py"))
    }

    private static void assertDeclareVariableOperatorPresent(File file) {
        assertDeclareVariableOperatorPresent(file, "")
    }

    private static void assertDeclareVariableOperatorPresent(File file, String qualifiedName) {
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundInitializeVariableOperator = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter(qualifiedName + "initializeVariableOperator()")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
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