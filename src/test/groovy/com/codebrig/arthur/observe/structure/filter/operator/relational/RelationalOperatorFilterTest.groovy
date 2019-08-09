package com.codebrig.arthur.observe.structure.filter.operator.relational

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.filter.FunctionFilter
import com.codebrig.arthur.observe.structure.filter.InternalRoleFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.RoleFilter
import com.codebrig.arthur.observe.structure.filter.TypeFilter
import com.codebrig.arthur.observe.structure.filter.operator.relational.define.DeclareVariableOperatorFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
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

    private static void assertRelationalOperatorPresent(File file) {
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.babelfishName, Encoding.UTF8$.MODULE$)

        def foundLeftOperands = false
        def foundRightOperands = false
        new FunctionFilter().getFilteredNodes(language, resp.uast).each {
            MultiFilter.matchAll(new RelationalOperatorFilter()).reject(new DeclareVariableOperatorFilter())
                    .getFilteredNodes(it).each {
                def left = null
                def right = null
                def matchedLeft = MultiFilter.matchAny(
                        new RoleFilter("LEFT"),
                        new InternalRoleFilter("Left")
                ).getFilteredNodes(it.children)
                left = (matchedLeft.hasNext()) ? matchedLeft.next() : left
                def matchedRight = MultiFilter.matchAny(
                        new RoleFilter("RIGHT"),
                        new InternalRoleFilter("Right")
                ).getFilteredNodes(it.children)
                right = (matchedRight.hasNext()) ? matchedRight.next() : right
                left = (left == null) ? getPropOperand1(it.children) : left
                right = (right == null) ? getPropOperand2(it.children) : right
                assertNotNull(left)
                assertNotNull(right)
                foundLeftOperands = true
                foundRightOperands = true
            }
        }
        assertTrue(foundLeftOperands)
        assertTrue(foundRightOperands)
    }

    private static SourceNode getPropOperand1(Iterator<SourceNode> children) {
        def node = null
        new TypeFilter("CPPASTBinaryExpression").getFilteredNodes(children).each {
            new InternalRoleFilter("Prop_Operand1").getFilteredNodes(it).each {
                node = it
            }
        }
        return node
    }

    private static SourceNode getPropOperand2(Iterator<SourceNode> children) {
        def node = null
        new TypeFilter("CPPASTBinaryExpression").getFilteredNodes(children).each {
            new InternalRoleFilter("Prop_Operand2").getFilteredNodes(it).each {
                node = it
            }
        }
        return node
    }
}