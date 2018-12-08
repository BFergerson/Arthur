package com.codebrig.omnisrc.observe.filter.operator.logical

import com.codebrig.omnisrc.OmniSRCTest
import com.codebrig.omnisrc.SourceLanguage
import com.codebrig.omnisrc.observe.filter.FunctionFilter
import com.codebrig.omnisrc.observe.filter.MultiFilter
import com.codebrig.omnisrc.observe.filter.NameFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.*

class AndOperatorFilterTest extends OmniSRCTest {

    @Test
    void andOperator_Go() {
        def file = new File("src/test/resources/same/operators/Operators.go")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundAndOperator = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("andOperator")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("andOperator()", it.name)

            new AndOperatorFilter().getFilteredNodes(it).each {
                assertFalse(foundAndOperator)
                assertEquals("&&", it.token)
                foundAndOperator = true
            }
        }
        assertTrue(foundAndOperator)
    }

    @Test
    void andOperator_Java() {
        def file = new File("src/test/resources/same/operators/Operators.java")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundAndOperator = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("andOperator")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("Operators.andOperator()", it.name)

            new AndOperatorFilter().getFilteredNodes(it).each {
                assertFalse(foundAndOperator)
                assertEquals("&&", it.token)
                foundAndOperator = true
            }
        }
        assertTrue(foundAndOperator)
    }

    @Test
    void andOperator_Javascript() {
        def file = new File("src/test/resources/same/operators/Operators.js")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundAndOperator = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("andOperator")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("andOperator()", it.name)

            new AndOperatorFilter().getFilteredNodes(it).each {
                assertFalse(foundAndOperator)
                assertEquals("&&", it.token)
                foundAndOperator = true
            }
        }
        assertTrue(foundAndOperator)
    }

    @Test
    void andOperator_Python() {
        def file = new File("src/test/resources/same/operators/Operators.py")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundAndOperator = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("andOperator")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("andOperator()", it.name)

            new AndOperatorFilter().getFilteredNodes(it).each {
                assertFalse(foundAndOperator)
                assertEquals("and", it.token)
                foundAndOperator = true
            }
        }
        assertTrue(foundAndOperator)
    }
}