package com.codebrig.arthur.observe.structure.filter.operator.relational

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.FunctionFilter
import com.codebrig.arthur.observe.structure.filter.InternalRoleFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.RoleFilter
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

    private static void assertRelationalOperatorPresent(File file) {
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundLeftOperands = false
        def foundRightOperands = false
        new FunctionFilter().getFilteredNodes(language, resp.uast).each {
            MultiFilter.matchAll(new RelationalOperatorFilter()).reject(new DeclareVariableOperatorFilter())
                    .getFilteredNodes(it).each {
                assertNotNull(
                        MultiFilter.matchAny(
                                new RoleFilter("LEFT"),
                                new InternalRoleFilter("Left")
                        ).getFilteredNodes(it.children).next()
                )
                assertNotNull(
                        MultiFilter.matchAny(
                                new RoleFilter("RIGHT"),
                                new InternalRoleFilter("Right")
                        ).getFilteredNodes(it.children).next()
                )
                foundLeftOperands = true
                foundRightOperands = true
            }
        }
        assertTrue(foundLeftOperands)
        assertTrue(foundRightOperands)
    }
}