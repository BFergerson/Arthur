package com.codebrig.arthur.observe.structure.filter.operator.relational

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.FunctionFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.operator.relational.define.DeclareVariableOperatorFilter
import org.bblfsh.client.v2.BblfshClient
import org.junit.Test

import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertTrue

class RelationalOperatorFilterTest extends ArthurTest {

    @Test
    void relationalOperator_Go() {
        assertRelationalOperatorPresent(new File("src/test/resources/same/operators/Operators.go"))
    }

    @Test
    void relationalOperator_Java() {
        assertRelationalOperatorPresent(new File("src/test/resources/same/operators/Operators.java"))
    }

    @Test
    void relationalOperator_Javascript() {
        assertRelationalOperatorPresent(new File("src/test/resources/same/operators/Operators.js"))
    }

    @Test
    void relationalOperator_Php() {
        assertRelationalOperatorPresent(new File("src/test/resources/same/operators/Operators.php"))
    }

    @Test
    void relationalOperator_Python() {
        assertRelationalOperatorPresent(new File("src/test/resources/same/operators/Operators.py"))
    }

    @Test
    void relationalOperator_CSharp() {
        assertRelationalOperatorPresent(new File("src/test/resources/same/operators/Operators.cs"))
    }

    @Test
    void relationalOperator_CPlusPlus() {
        assertRelationalOperatorPresent(new File("src/test/resources/same/operators/Operators.cpp"))
    }

    @Test
    void relationalOperator_Ruby() {
        assertRelationalOperatorPresent(new File("src/test/resources/same/operators/Operators.rb"))
    }

    @Test
    void relationalOperator_Bash() {
        assertRelationalOperatorPresent(new File("src/test/resources/same/operators/Operators.sh"))
    }

    private static void assertRelationalOperatorPresent(File file) {
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.babelfishName)
        def rootNode = new BblfshClient.UastMethods(resp.uast()).decode().root().load()

        def foundLeftOperands = false
        def foundRightOperands = false
        new FunctionFilter().getFilteredNodes(language, rootNode).each {
            MultiFilter.matchAll(new RelationalOperatorFilter()).reject(new DeclareVariableOperatorFilter())
                    .getFilteredNodes(it).each {
                assertNotNull(RelationalOperatorFilter.getLeftOperand(it))
                assertNotNull(RelationalOperatorFilter.getRightOperand(it))
                foundLeftOperands = true
                foundRightOperands = true
            }
        }
        assertTrue(foundLeftOperands)
        assertTrue(foundRightOperands)
    }
}