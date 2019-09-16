package com.codebrig.arthur.observe.structure.filter.conditional

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.FunctionFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.NameFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.*

class ElseIfConditionalFilterTest extends ArthurTest {

    @Test
    void elseIfConditional_Go() {
        assertElseIfConditionalPresent(new File("src/test/resources/same/conditionals/Conditionals.go"))
    }

    @Test
    void elseIfConditional_Java() {
        assertElseIfConditionalPresent(new File("src/test/resources/same/conditionals/Conditionals.java"),
                "Conditionals.")
    }

    @Test
    void elseIfConditional_Javascript() {
        assertElseIfConditionalPresent(new File("src/test/resources/same/conditionals/Conditionals.js"))
    }

    @Test
    void elseIfConditional_Python() {
        assertElseIfConditionalPresent(new File("src/test/resources/same/conditionals/Conditionals.py"))
    }

    @Test
    void elseIfConditional_CSharp() {
        assertElseIfConditionalPresent(new File("src/test/resources/same/conditionals/Conditionals.cs"))
    }

    @Test
    void elseIfConditional_Bash() {
        assertElseIfConditionalPresent(new File("src/test/resources/same/conditionals/Conditionals.sh"))
    }

    private static void assertElseIfConditionalPresent(File file) {
        assertElseIfConditionalPresent(file, "")
    }

    private static void assertElseIfConditionalPresent(File file, String qualifiedName) {
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundElseIfConditional = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter(qualifiedName + "ifElseIfConditional()")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals(qualifiedName + "ifElseIfConditional()", it.name)

            new ElseIfConditionalFilter().getFilteredNodes(it).each {
                assertFalse(foundElseIfConditional)
                foundElseIfConditional = true
            }
        }
        assertTrue(foundElseIfConditional)
    }
}