package com.codebrig.omnisrc.observe.structure.filter.loop

import com.codebrig.omnisrc.OmniSRCTest
import com.codebrig.omnisrc.SourceLanguage
import com.codebrig.omnisrc.observe.structure.filter.FunctionFilter
import com.codebrig.omnisrc.observe.structure.filter.MultiFilter
import com.codebrig.omnisrc.observe.structure.filter.NameFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.*

class ForEachLoopFilterTest extends OmniSRCTest {

    @Test
    void forEachLoop_Go() {
        def file = new File("src/test/resources/same/loops/Loops.go")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundForEachLoop = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("forEachLoop")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("forEachLoop()", it.name)

            new ForEachLoopFilter().getFilteredNodes(it).each {
                assertFalse(foundForEachLoop)
                foundForEachLoop = true
            }
        }
        assertTrue(foundForEachLoop)
    }

    @Test
    void forEachLoop_Java() {
        def file = new File("src/test/resources/same/loops/Loops.java")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundForEachLoop = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("forEachLoop")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("Loops.forEachLoop()", it.name)

            new ForEachLoopFilter().getFilteredNodes(it).each {
                assertFalse(foundForEachLoop)
                foundForEachLoop = true
            }
        }
        assertTrue(foundForEachLoop)
    }

    @Test
    void forEachLoop_Javascript() {
        def file = new File("src/test/resources/same/loops/Loops.js")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundForEachLoop = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("forEachLoop")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("forEachLoop()", it.name)

            new ForEachLoopFilter().getFilteredNodes(it).each {
                assertFalse(foundForEachLoop)
                foundForEachLoop = true
            }
        }
        assertTrue(foundForEachLoop)
    }
}