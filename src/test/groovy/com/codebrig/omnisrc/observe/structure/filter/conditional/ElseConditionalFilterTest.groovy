package com.codebrig.omnisrc.observe.structure.filter.conditional

import com.codebrig.omnisrc.OmniSRCTest
import com.codebrig.omnisrc.SourceLanguage
import com.codebrig.omnisrc.observe.structure.filter.FunctionFilter
import com.codebrig.omnisrc.observe.structure.filter.MultiFilter
import com.codebrig.omnisrc.observe.structure.filter.NameFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.*

class ElseConditionalFilterTest extends OmniSRCTest {

    @Test
    void elseConditional_Go() {
        def file = new File("src/test/resources/same/conditionals/Conditionals.go")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundElseConditional = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("ifElseConditional")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("ifElseConditional()", it.name)

            new ElseConditionalFilter().getFilteredNodes(it).each {
                assertFalse(foundElseConditional)
                foundElseConditional = true
            }
        }
        assertTrue(foundElseConditional)
    }

    @Test
    void elseConditional_Java() {
        def file = new File("src/test/resources/same/conditionals/Conditionals.java")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundElseConditional = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("ifElseConditional")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("Conditionals.ifElseConditional()", it.name)

            new ElseConditionalFilter().getFilteredNodes(it).each {
                assertFalse(foundElseConditional)
                foundElseConditional = true
            }
        }
        assertTrue(foundElseConditional)
    }

    @Test
    void elseConditional_Javascript() {
        def file = new File("src/test/resources/same/conditionals/Conditionals.js")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundElseConditional = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("ifElseConditional")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("ifElseConditional()", it.name)

            new ElseConditionalFilter().getFilteredNodes(it).each {
                assertFalse(foundElseConditional)
                foundElseConditional = true
            }
        }
        assertTrue(foundElseConditional)
    }

    @Test
    void elseConditional_Python() {
        def file = new File("src/test/resources/same/conditionals/Conditionals.py")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundElseConditional = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("ifElseConditional")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("ifElseConditional()", it.name)

            new ElseConditionalFilter().getFilteredNodes(it).each {
                assertFalse(foundElseConditional)
                foundElseConditional = true
            }
        }
        assertTrue(foundElseConditional)
    }
}