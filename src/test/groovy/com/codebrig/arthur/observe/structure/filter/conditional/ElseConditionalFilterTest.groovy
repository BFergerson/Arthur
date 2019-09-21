package com.codebrig.arthur.observe.structure.filter.conditional

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.FunctionFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.NameFilter
import org.junit.Test

import static org.junit.Assert.*

class ElseConditionalFilterTest extends ArthurTest {

    @Test
    void elseConditional_Go() {
        assertElseConditionalPresent(new File("src/test/resources/same/conditionals/Conditionals.go"))
    }

    @Test
    void elseConditional_Java() {
        assertElseConditionalPresent(new File("src/test/resources/same/conditionals/Conditionals.java"),
                "Conditionals.")
    }

    @Test
    void elseConditional_Javascript() {
        assertElseConditionalPresent(new File("src/test/resources/same/conditionals/Conditionals.js"))
    }

    @Test
    void elseConditional_Python() {
        assertElseConditionalPresent(new File("src/test/resources/same/conditionals/Conditionals.py"))
    }

    @Test
    void elseConditional_CSharp() {
        assertElseConditionalPresent(new File("src/test/resources/same/conditionals/Conditionals.cs"))
    }

    @Test
    void elseConditional_CPlusPlus() {
        assertElseConditionalPresent(new File("src/test/resources/same/conditionals/Conditionals.cpp"))
    }

    private static void assertElseConditionalPresent(File file) {
        assertElseConditionalPresent(file, "")
    }

    private static void assertElseConditionalPresent(File file, String qualifiedName) {
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.babelfishName)

        def foundElseConditional = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter(qualifiedName + "ifElseConditional()")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals(qualifiedName + "ifElseConditional()", it.name)

            new ElseConditionalFilter().getFilteredNodes(it).each {
                assertFalse(foundElseConditional)
                foundElseConditional = true
            }
        }
        assertTrue(foundElseConditional)
    }
}