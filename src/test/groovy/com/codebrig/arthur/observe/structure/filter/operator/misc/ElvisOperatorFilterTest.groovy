package com.codebrig.arthur.observe.structure.filter.operator.misc

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.FunctionFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.NameFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.*

class ElvisOperatorFilterTest extends ArthurTest {

    @Test
    void elvisOperator_Php() {
        assertElvisOperatorPresent(new File("src/test/resources/same/operators/Operators.php"))
    }

    private static void assertElvisOperatorPresent(File file) {
        assertElvisOperatorPresent(file, "")
    }

    private static void assertElvisOperatorPresent(File file, String qualifiedName) {
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundElvisOperator = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("elvisOperator")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals(qualifiedName + "elvisOperator()", it.name)

            new ElvisOperatorFilter().getFilteredNodes(it).each {
                assertFalse(foundElvisOperator)
                foundElvisOperator = true
            }
        }
        assertTrue(foundElvisOperator)
    }
}
