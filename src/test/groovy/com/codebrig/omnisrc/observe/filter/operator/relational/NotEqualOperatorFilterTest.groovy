package com.codebrig.omnisrc.observe.filter.operator.relational

import com.codebrig.omnisrc.OmniSRCTest
import com.codebrig.omnisrc.SourceLanguage
import com.codebrig.omnisrc.observe.filter.FunctionFilter
import com.codebrig.omnisrc.observe.filter.MultiFilter
import com.codebrig.omnisrc.observe.filter.NameFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.*

class NotEqualOperatorFilterTest extends OmniSRCTest {

    @Test
    void notEqualOperator_Go() {
        def file = new File("src/test/resources/same/operators/Operators.go")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundNotEqualOperator = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("notEqualOperator")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("notEqualOperator()", it.name)

            new NotEqualOperatorFilter().getFilteredNodes(it).each {
                assertFalse(foundNotEqualOperator)
                assertEquals("!=", it.token)
                foundNotEqualOperator = true
            }
        }
        assertTrue(foundNotEqualOperator)
    }

    @Test
    void notEqualOperator_Java() {
        def file = new File("src/test/resources/same/operators/Operators.java")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundNotEqualOperator = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("notEqualOperator")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("Operators.notEqualOperator()", it.name)

            new NotEqualOperatorFilter().getFilteredNodes(it).each {
                assertFalse(foundNotEqualOperator)
                assertEquals("!=", it.token)
                foundNotEqualOperator = true
            }
        }
        assertTrue(foundNotEqualOperator)
    }

    @Test
    void notEqualOperator_Javascript() {
        def file = new File("src/test/resources/same/operators/Operators.js")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundNotEqualOperator = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("notEqualOperator")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("notEqualOperator()", it.name)

            new NotEqualOperatorFilter().getFilteredNodes(it).each {
                assertFalse(foundNotEqualOperator)
                assertEquals("!=", it.token)
                foundNotEqualOperator = true
            }
        }
        assertTrue(foundNotEqualOperator)
    }

    @Test
    void notEqualOperator_Python() {
        def file = new File("src/test/resources/same/operators/Operators.py")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundNotEqualOperator = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("notEqualOperator")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("notEqualOperator()", it.name)

            new NotEqualOperatorFilter().getFilteredNodes(it).each {
                assertFalse(foundNotEqualOperator)
                assertEquals("!=", it.token)
                foundNotEqualOperator = true
            }
        }
        assertTrue(foundNotEqualOperator)
    }

    @Test
    void alternateNotEqualOperator_Python() {
        def file = new File("src/test/resources/same/operators/Operators.py")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundAlternateNotEqualOperator = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("alternateNotEqualOperator")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("alternateNotEqualOperator()", it.name)

            new NotEqualOperatorFilter().getFilteredNodes(it).each {
                assertFalse(foundAlternateNotEqualOperator)
                assertEquals("!=", it.token)
                foundAlternateNotEqualOperator = true
            }
        }
        assertTrue(foundAlternateNotEqualOperator)
    }
}