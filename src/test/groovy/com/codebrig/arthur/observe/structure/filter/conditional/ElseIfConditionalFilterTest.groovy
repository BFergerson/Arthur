package com.codebrig.arthur.observe.structure.filter.conditional

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.FunctionFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.NameFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.*

class ElseIfConditionalFilterTest extends ArthurTest {

    @Test
    void elseIfConditional_Go() {
        def file = new File("src/test/resources/same/conditionals/Conditionals.go")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundElseIfConditional = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("ifElseIfConditional")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("ifElseIfConditional()", it.name)

            new ElseIfConditionalFilter().getFilteredNodes(it).each {
                assertFalse(foundElseIfConditional)
                foundElseIfConditional = true
            }
        }
        assertTrue(foundElseIfConditional)
    }

    @Test
    void elseIfConditional_Java() {
        def file = new File("src/test/resources/same/conditionals/Conditionals.java")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundElseIfConditional = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("ifElseIfConditional")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("Conditionals.ifElseIfConditional()", it.name)

            new ElseIfConditionalFilter().getFilteredNodes(it).each {
                assertFalse(foundElseIfConditional)
                foundElseIfConditional = true
            }
        }
        assertTrue(foundElseIfConditional)
    }

    @Test
    void elseIfConditional_Javascript() {
        def file = new File("src/test/resources/same/conditionals/Conditionals.js")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundElseIfConditional = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("ifElseIfConditional")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("ifElseIfConditional()", it.name)

            new ElseIfConditionalFilter().getFilteredNodes(it).each {
                assertFalse(foundElseIfConditional)
                foundElseIfConditional = true
            }
        }
        assertTrue(foundElseIfConditional)
    }

    @Test
    void elseIfConditional_Python() {
        def file = new File("src/test/resources/same/conditionals/Conditionals.py")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundElseIfConditional = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("ifElseIfConditional")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("ifElseIfConditional()", it.name)

            new ElseIfConditionalFilter().getFilteredNodes(it).each {
                assertFalse(foundElseIfConditional)
                foundElseIfConditional = true
            }
        }
        assertTrue(foundElseIfConditional)
    }
}