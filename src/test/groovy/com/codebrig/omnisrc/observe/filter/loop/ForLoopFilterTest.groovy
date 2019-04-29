package com.codebrig.omnisrc.observe.filter.loop

import com.codebrig.omnisrc.OmniSRCTest
import com.codebrig.omnisrc.SourceLanguage
import com.codebrig.omnisrc.observe.filter.FunctionFilter
import com.codebrig.omnisrc.observe.filter.MultiFilter
import com.codebrig.omnisrc.observe.filter.NameFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.*

class ForLoopFilterTest extends OmniSRCTest {

    @Test
    void forLoop_Go() {
        def file = new File("src/test/resources/same/loops/Loops.go")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundForLoop = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("forLoop")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("forLoop()", it.name)

            new ForLoopFilter().getFilteredNodes(it).each {
                assertFalse(foundForLoop)
                foundForLoop = true
            }
        }
        assertTrue(foundForLoop)
    }

    @Test
    void forLoop_Java() {
        def file = new File("src/test/resources/same/loops/Loops.java")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundForLoop = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("forLoop")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("Loops.forLoop()", it.name)

            new ForLoopFilter().getFilteredNodes(it).each {
                assertFalse(foundForLoop)
                foundForLoop = true
            }
        }
        assertTrue(foundForLoop)
    }

    @Test
    void forLoop_Javascript() {
        def file = new File("src/test/resources/same/loops/Loops.js")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundForLoop = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("forLoop")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("forLoop()", it.name)

            new ForLoopFilter().getFilteredNodes(it).each {
                assertFalse(foundForLoop)
                foundForLoop = true
            }
        }
        assertTrue(foundForLoop)
    }

    @Test
    void forLoop_Php() {
        def file = new File("src/test/resources/same/loops/Loops.php")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundForLoop = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("forLoop")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("forLoop()", it.name)

            new ForLoopFilter().getFilteredNodes(it).each {
                assertFalse(foundForLoop)
                foundForLoop = true
            }
        }
        assertTrue(foundForLoop)
    }

    @Test
    void forLoop_Python() {
        def file = new File("src/test/resources/same/loops/Loops.py")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundForLoop = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("forLoop")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("forLoop()", it.name)

            new ForLoopFilter().getFilteredNodes(it).each {
                assertFalse(foundForLoop)
                foundForLoop = true
            }
        }
        assertTrue(foundForLoop)
    }

    @Test
    void forLoop_Ruby() {
        def file = new File("src/test/resources/same/loops/Loops.rb")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundForLoop = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("forLoop")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("forLoop()", it.name)

            new ForLoopFilter().getFilteredNodes(it).each {
                assertFalse(foundForLoop)
                foundForLoop = true
            }
        }
        assertTrue(foundForLoop)
    }
}