package com.codebrig.arthur.observe.structure.filter.conditional

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.FunctionFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.NameFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.*

class IfConditionalFilterTest extends ArthurTest {

    @Test
    void ifConditional_Go() {
        assertIfConditionalPresent(new File("src/test/resources/same/conditionals/Conditionals.go"))
    }

    @Test
    void ifConditional_Java() {
        assertIfConditionalPresent(new File("src/test/resources/same/conditionals/Conditionals.java"),
                "Conditionals.")
    }

    @Test
    void ifConditional_Javascript() {
        assertIfConditionalPresent(new File("src/test/resources/same/conditionals/Conditionals.js"))
    }

    @Test
    void ifConditional_Python() {
        assertIfConditionalPresent(new File("src/test/resources/same/conditionals/Conditionals.py"))
    }

    @Test
    void ifConditional_Php() {
        assertIfConditionalPresent(new File("src/test/resources/same/conditionals/Conditionals.php"))
    }

    @Test
    void ifConditional_CSharp() {
        assertIfConditionalPresent(new File("src/test/resources/same/conditionals/Conditionals.cs"))
    }

    @Test
    void ifConditional_CPlusPlus() {
        assertIfConditionalPresent(new File("src/test/resources/same/conditionals/Conditionals.cpp"))
    }

    @Test
    void ifConditional_Ruby() {
        assertIfConditionalPresent(new File("src/test/resources/same/conditionals/Conditionals.rb"))
        assertIfConditionalPresent(new File("src/test/resources/same/conditionals/Conditionals.rb"),
                "", "unlessConditional()")
    }

    @Test
    void ifConditional_Bash() {
        assertIfConditionalPresent(new File("src/test/resources/same/conditionals/Conditionals.sh"))
    }

    private static void assertIfConditionalPresent(File file) {
        assertIfConditionalPresent(file, "", "ifConditional()")
    }

    private static void assertIfConditionalPresent(File file, String qualifiedName) {
        assertIfConditionalPresent(file, qualifiedName, "ifConditional()")
    }

    private static void assertIfConditionalPresent(File file, String qualifiedName, String functionName) {
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.babelfishName, Encoding.UTF8$.MODULE$)

        def foundIfConditional = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter(qualifiedName + functionName)
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals(qualifiedName + functionName, it.name)

            new IfConditionalFilter().getFilteredNodes(it).each {
                assertFalse(foundIfConditional)
                foundIfConditional = true
            }
        }
        assertTrue(foundIfConditional)
    }
}