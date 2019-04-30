package com.codebrig.omnisrc.observe.structure.filter.operator.relational

import com.codebrig.omnisrc.OmniSRCTest
import com.codebrig.omnisrc.SourceLanguage
import com.codebrig.omnisrc.observe.structure.filter.FunctionFilter
import com.codebrig.omnisrc.observe.structure.filter.MultiFilter
import com.codebrig.omnisrc.observe.structure.filter.NameFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.*

class EqualOperatorFilterTest extends OmniSRCTest {

    @Test
    void equalOperator_Go() {
        assertEqualOperatorPresent(new File("src/test/resources/same/operators/Operators.go"))
    }

    @Test
    void equalOperator_Java() {
        assertEqualOperatorPresent(new File("src/test/resources/same/operators/Operators.java"),
                "Operators.")
    }

    @Test
    void equalOperator_Javascript() {
        assertEqualOperatorPresent(new File("src/test/resources/same/operators/Operators.js"))
    }

    @Test
    void equalOperator_Php() {
        assertEqualOperatorPresent(new File("src/test/resources/same/operators/Operators.php"))
    }

    @Test
    void equalOperator_Python() {
        assertEqualOperatorPresent(new File("src/test/resources/same/operators/Operators.py"))
    }

    private static void assertEqualOperatorPresent(File file) {
        assertEqualOperatorPresent(file, "")
    }

    private static void assertEqualOperatorPresent(File file, String qualifiedName) {
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundEqualOperator = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("equalOperator")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals(qualifiedName + "equalOperator()", it.name)

            new EqualOperatorFilter().getFilteredNodes(it).each {
                assertFalse(foundEqualOperator)
                if (!it.token.isEmpty()) assertEquals("==", it.token)
                foundEqualOperator = true
            }
        }
        assertTrue(foundEqualOperator)
    }
}