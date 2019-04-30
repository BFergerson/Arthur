package com.codebrig.omnisrc.observe.structure.filter.loop

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
        assertLoopPresent(new File("src/test/resources/same/loops/Loops.go"))
    }

    @Test
    void forLoop_Java() {
        assertLoopPresent(new File("src/test/resources/same/loops/Loops.java"), "Loops.")
    }

    @Test
    void forLoop_Javascript() {
        assertLoopPresent(new File("src/test/resources/same/loops/Loops.js"))
    }

    @Test
    void forLoop_Php() {
        assertLoopPresent(new File("src/test/resources/same/loops/Loops.php"))
    }

    @Test
    void forLoop_Python() {
        assertLoopPresent(new File("src/test/resources/same/loops/Loops.py"))
    }

    @Test
    void forLoop_Ruby() {
        assertLoopPresent(new File("src/test/resources/same/loops/Loops.rb"))
    }

    private static void assertLoopPresent(File file) {
        assertLoopPresent(file, "")
    }

    private static void assertLoopPresent(File file, String qualifiedName) {
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundForLoop = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("forLoop")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals(qualifiedName + "forLoop()", it.name)

            new ForLoopFilter().getFilteredNodes(it).each {
                assertFalse(foundForLoop)
                foundForLoop = true
            }
        }
        assertTrue(foundForLoop)
    }
}