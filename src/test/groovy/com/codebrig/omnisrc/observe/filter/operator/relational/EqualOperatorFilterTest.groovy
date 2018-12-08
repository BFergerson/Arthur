package com.codebrig.omnisrc.observe.filter.operator.relational

import com.codebrig.omnisrc.OmniSRCTest
import com.codebrig.omnisrc.SourceLanguage
import com.codebrig.omnisrc.observe.filter.FunctionFilter
import com.codebrig.omnisrc.observe.filter.MultiFilter
import com.codebrig.omnisrc.observe.filter.NameFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.*

class EqualOperatorFilterTest extends OmniSRCTest {

    @Test
    void equalOperator_Go() {
        def file = new File("src/test/resources/same/operators/Operators.go")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundEqualOperator = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("equalOperator")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("equalOperator()", it.name)

            new EqualOperatorFilter().getFilteredNodes(it).each {
                assertFalse(foundEqualOperator)
                assertEquals("==", it.token)
                foundEqualOperator = true
            }
        }
        assertTrue(foundEqualOperator)
    }

    @Test
    void equalOperator_Java() {
        def file = new File("src/test/resources/same/operators/Operators.java")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundEqualOperator = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("equalOperator")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("Operators.equalOperator()", it.name)

            new EqualOperatorFilter().getFilteredNodes(it).each {
                assertFalse(foundEqualOperator)
                assertEquals("==", it.token)
                foundEqualOperator = true
            }
        }
        assertTrue(foundEqualOperator)
    }

    @Test
    void equalOperator_Javascript() {
        def file = new File("src/test/resources/same/operators/Operators.js")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundEqualOperator = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("equalOperator")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("equalOperator()", it.name)

            new EqualOperatorFilter().getFilteredNodes(it).each {
                assertFalse(foundEqualOperator)
                assertEquals("==", it.token)
                foundEqualOperator = true
            }
        }
        assertTrue(foundEqualOperator)
    }

    @Test
    void equalOperator_Python() {
        def file = new File("src/test/resources/same/operators/Operators.py")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundEqualOperator = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("equalOperator")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("equalOperator()", it.name)

            new EqualOperatorFilter().getFilteredNodes(it).each {
                assertFalse(foundEqualOperator)
                assertEquals("==", it.token)
                foundEqualOperator = true
            }
        }
        assertTrue(foundEqualOperator)
    }
}