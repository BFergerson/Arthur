package com.codebrig.omnisrc.observe.structure.filter.operator.misc

import com.codebrig.omnisrc.OmniSRCTest
import com.codebrig.omnisrc.SourceLanguage
import com.codebrig.omnisrc.observe.structure.filter.FunctionFilter
import com.codebrig.omnisrc.observe.structure.filter.MultiFilter
import com.codebrig.omnisrc.observe.structure.filter.NameFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.*

class TernaryOperatorFilterTest extends OmniSRCTest {

    @Test
    void ternaryOperator_Java() {
        def file = new File("src/test/resources/same/operators/Operators.java")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundTernaryOperator = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("ternaryOperator")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("Operators.ternaryOperator()", it.name)

            new TernaryOperatorFilter().getFilteredNodes(it).each {
                assertFalse(foundTernaryOperator)
                foundTernaryOperator = true
            }
        }
        assertTrue(foundTernaryOperator)
    }

    @Test
    void ternaryOperator_Javascript() {
        def file = new File("src/test/resources/same/operators/Operators.js")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundTernaryOperator = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("ternaryOperator")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("ternaryOperator()", it.name)

            new TernaryOperatorFilter().getFilteredNodes(it).each {
                assertFalse(foundTernaryOperator)
                foundTernaryOperator = true
            }
        }
        assertTrue(foundTernaryOperator)
    }
}