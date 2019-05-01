package com.codebrig.arthur.observe.structure.filter.loop

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.FunctionFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.NameFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.*

class DoWhileLoopFilterTest extends ArthurTest {

    @Test
    void doWhileLoop_Java() {
        def file = new File("src/test/resources/same/loops/Loops.java")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundDoWhileLoop = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("doWhileLoop")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("Loops.doWhileLoop()", it.name)

            new DoWhileLoopFilter().getFilteredNodes(it).each {
                assertFalse(foundDoWhileLoop)
                foundDoWhileLoop = true
            }
        }
        assertTrue(foundDoWhileLoop)
    }

    @Test
    void doWhileLoop_Javascript() {
        def file = new File("src/test/resources/same/loops/Loops.js")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundDoWhileLoop = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("doWhileLoop")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("doWhileLoop()", it.name)

            new DoWhileLoopFilter().getFilteredNodes(it).each {
                assertFalse(foundDoWhileLoop)
                foundDoWhileLoop = true
            }
        }
        assertTrue(foundDoWhileLoop)
    }
}