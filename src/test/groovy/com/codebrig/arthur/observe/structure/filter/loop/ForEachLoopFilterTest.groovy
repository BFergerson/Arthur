package com.codebrig.arthur.observe.structure.filter.loop

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.FunctionFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.NameFilter
import org.junit.Test

import static org.junit.Assert.*

class ForEachLoopFilterTest extends ArthurTest {

    @Test
    void forEachLoop_Go() {
        assertForEachLoopPresent(new File("src/test/resources/same/loops/Loops.go"))
    }

    @Test
    void forEachLoop_Java() {
        assertForEachLoopPresent(new File("src/test/resources/same/loops/Loops.java"),
                "Loops.")
    }

    @Test
    void forEachLoop_Javascript() {
        assertForEachLoopPresent(new File("src/test/resources/same/loops/Loops.js"))
    }

    @Test
    void forEachLoop_CSharp() {
        assertForEachLoopPresent(new File("src/test/resources/same/loops/Loops.cs"))
    }

    @Test
    void forEachLoop_CPlusPlus() {
        assertForEachLoopPresent(new File("src/test/resources/same/loops/Loops.cpp"))
    }

    private static void assertForEachLoopPresent(File file) {
        assertForEachLoopPresent(file, "")
    }

    private static void assertForEachLoopPresent(File file, String qualifiedName) {
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.babelfishName)

        def foundForEachLoop = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter(qualifiedName + "forEachLoop()")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals(qualifiedName + "forEachLoop()", it.name)

            new ForEachLoopFilter().getFilteredNodes(it).each {
                assertFalse(foundForEachLoop)
                foundForEachLoop = true
            }
        }
        assertTrue(foundForEachLoop)
    }
}