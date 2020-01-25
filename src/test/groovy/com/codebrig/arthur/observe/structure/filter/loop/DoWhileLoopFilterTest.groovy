package com.codebrig.arthur.observe.structure.filter.loop

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.FunctionFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.NameFilter
import org.bblfsh.client.v2.BblfshClient
import org.junit.Test

import static org.junit.Assert.*

class DoWhileLoopFilterTest extends ArthurTest {

    @Test
    void doWhileLoop_Java() {
        assertDoWhileLoopPresent(new File("src/test/resources/same/loops/Loops.java"),"Loops.")
    }

    @Test
    void doWhileLoop_Javascript() {
        assertDoWhileLoopPresent(new File("src/test/resources/same/loops/Loops.js"))
    }

    @Test
    void doWhileLoop_CSharp() {
        assertDoWhileLoopPresent(new File("src/test/resources/same/loops/Loops.cs"))
    }

    @Test
    void doWhileLoop_CPlusPlus() {
        assertDoWhileLoopPresent(new File("src/test/resources/same/loops/Loops.cpp"))
    }

    private static void assertDoWhileLoopPresent(File file) {
        assertDoWhileLoopPresent(file, "")
    }

    private static void assertDoWhileLoopPresent(File file, String qualifiedName) {
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.babelfishName)
        def rootNode = new BblfshClient.UastMethods(resp.uast()).decode().root().load()

        def foundDoWhileLoop = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter(qualifiedName + "doWhileLoop()")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, rootNode).each {
            assertEquals(qualifiedName + "doWhileLoop()", it.name)

            new DoWhileLoopFilter().getFilteredNodes(it).each {
                assertFalse(foundDoWhileLoop)
                foundDoWhileLoop = true
            }
        }
        assertTrue(foundDoWhileLoop)
    }
}