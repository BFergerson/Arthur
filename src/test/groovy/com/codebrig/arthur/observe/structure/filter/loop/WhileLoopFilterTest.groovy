package com.codebrig.arthur.observe.structure.filter.loop

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.FunctionFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.NameFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.*

class WhileLoopFilterTest extends ArthurTest {

    @Test
    void whileLoop_Go() {
        assertWhileLoopPresent(new File("src/test/resources/same/loops/Loops.go"))
    }

    @Test
    void whileLoop_Java() {
        assertWhileLoopPresent(new File("src/test/resources/same/loops/Loops.java"),
                "Loops.")
    }

    @Test
    void whileLoop_Javascript() {
        assertWhileLoopPresent(new File("src/test/resources/same/loops/Loops.js"))
    }

    @Test
    void whileLoop_Python() {
        assertWhileLoopPresent(new File("src/test/resources/same/loops/Loops.py"))
    }

    @Test
    void whileLoop_CSharp() {
        assertWhileLoopPresent(new File("src/test/resources/same/loops/Loops.cs"))
    }

    private static void assertWhileLoopPresent(File file) {
        assertWhileLoopPresent(file, "")
    }

    private static void assertWhileLoopPresent(File file, String qualifiedName) {
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundWhileLoop = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter(qualifiedName + "whileLoop()")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals(qualifiedName + "whileLoop()", it.name)

            new WhileLoopFilter().getFilteredNodes(it).each {
                assertFalse(foundWhileLoop)
                foundWhileLoop = true
            }
        }
        assertTrue(foundWhileLoop)
    }
}