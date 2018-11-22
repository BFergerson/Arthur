package com.codebrig.omnisrc.observe.filter.operator.relational

import com.codebrig.omnisrc.OmniSRCTest
import com.codebrig.omnisrc.SourceLanguage
import com.codebrig.omnisrc.observe.filter.FunctionFilter
import com.codebrig.omnisrc.observe.filter.MultiFilter
import com.codebrig.omnisrc.observe.filter.NameFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.*

class EqualTypeOperatorFilterTest extends OmniSRCTest {

    @Test
    void equalTypeOperator_Javascript() {
        def file = new File("src/test/resources/same/operators/Operators.js")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundEqualTypeOperator = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("equalTypeOperator")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("equalTypeOperator()", it.name)

            new EqualTypeOperatorFilter().getFilteredNodes(it).each {
                assertFalse(foundEqualTypeOperator)
                assertEquals("===", it.token)
                foundEqualTypeOperator = true
            }
        }
        assertTrue(foundEqualTypeOperator)
    }
}