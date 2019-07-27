package com.codebrig.arthur.observe.structure.filter.loop

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.FunctionFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.NameFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.*

class ForLoopFilterTest extends ArthurTest {

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

    @Test
    void forLoop_CSharp() {
        assertLoopPresent(new File("src/test/resources/same/loops/Loops.cs"))
    }

    private static void assertLoopPresent(File file) {
        assertLoopPresent(file, "")
    }

    private static void assertLoopPresent(File file, String qualifiedName) {
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundForLoop = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter(qualifiedName + "forLoop()")
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