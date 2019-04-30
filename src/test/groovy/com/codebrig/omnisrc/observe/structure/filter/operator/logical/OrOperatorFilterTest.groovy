package com.codebrig.omnisrc.observe.structure.filter.operator.logical

import com.codebrig.omnisrc.OmniSRCTest
import com.codebrig.omnisrc.SourceLanguage
import com.codebrig.omnisrc.observe.structure.filter.FunctionFilter
import com.codebrig.omnisrc.observe.structure.filter.MultiFilter
import com.codebrig.omnisrc.observe.structure.filter.NameFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.*

class OrOperatorFilterTest extends OmniSRCTest {

    @Test
    void orOperator_Go() {
        def file = new File("src/test/resources/same/operators/Operators.go")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundOrOperator = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("orOperator")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("orOperator()", it.name)

            new OrOperatorFilter().getFilteredNodes(it).each {
                assertFalse(foundOrOperator)
                assertEquals("||", it.token)
                foundOrOperator = true
            }
        }
        assertTrue(foundOrOperator)
    }

    @Test
    void orOperator_Java() {
        def file = new File("src/test/resources/same/operators/Operators.java")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundOrOperator = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("orOperator")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("Operators.orOperator()", it.name)

            new OrOperatorFilter().getFilteredNodes(it).each {
                assertFalse(foundOrOperator)
                assertEquals("||", it.token)
                foundOrOperator = true
            }
        }
        assertTrue(foundOrOperator)
    }

    @Test
    void orOperator_Javascript() {
        def file = new File("src/test/resources/same/operators/Operators.js")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundOrOperator = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("orOperator")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("orOperator()", it.name)

            new OrOperatorFilter().getFilteredNodes(it).each {
                assertFalse(foundOrOperator)
                assertEquals("||", it.token)
                foundOrOperator = true
            }
        }
        assertTrue(foundOrOperator)
    }

    @Test
    void orOperator_Python() {
        def file = new File("src/test/resources/same/operators/Operators.py")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundOrOperator = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("orOperator")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("orOperator()", it.name)

            new OrOperatorFilter().getFilteredNodes(it).each {
                assertFalse(foundOrOperator)
                assertEquals("or", it.token)
                foundOrOperator = true
            }
        }
        assertTrue(foundOrOperator)
    }
}