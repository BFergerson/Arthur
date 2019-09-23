package com.codebrig.arthur.observe.structure.filter.conditional

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.FunctionFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.NameFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.*

class SwitchConditionalFilterTest extends ArthurTest {

    @Test
    void switchConditional_Go() {
        assertSwitchConditionalPresent(new File("src/test/resources/same/conditionals/Conditionals.go"))
    }

    @Test
    void switchConditional_Java() {
        assertSwitchConditionalPresent(new File("src/test/resources/same/conditionals/Conditionals.java"),
                "Conditionals.")
    }

    @Test
    void switchConditional_Javascript() {
        assertSwitchConditionalPresent(new File("src/test/resources/same/conditionals/Conditionals.js"))
    }

    @Test
    void switchConditional_CSharp() {
        assertSwitchConditionalPresent(new File("src/test/resources/same/conditionals/Conditionals.cs"))
    }

    @Test
    void switchConditional_CPlusPlus() {
        assertSwitchConditionalPresent(new File("src/test/resources/same/conditionals/Conditionals.cpp"))
    }

    @Test
    void switchConditional_Bash() {
        assertSwitchConditionalPresent(new File("src/test/resources/same/conditionals/Conditionals.sh"))
    }

    private static void assertSwitchConditionalPresent(File file) {
        assertSwitchConditionalPresent(file, "")
    }

    private static void assertSwitchConditionalPresent(File file, String qualifiedName) {
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.babelfishName, Encoding.UTF8$.MODULE$)

        def foundSwitchConditional = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter(qualifiedName + "switchConditional()")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals(qualifiedName + "switchConditional()", it.name)

            new SwitchConditionalFilter().getFilteredNodes(it).each {
                assertFalse(foundSwitchConditional)
                foundSwitchConditional = true
            }
        }
        assertTrue(foundSwitchConditional)
    }
}