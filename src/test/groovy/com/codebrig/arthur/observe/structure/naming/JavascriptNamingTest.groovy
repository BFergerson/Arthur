package com.codebrig.arthur.observe.structure.naming

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.FunctionFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.NameFilter
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

class JavascriptNamingTest extends ArthurTest {

    @Test
    void noArgs() {
        assertJavascriptNamingPresent("function1", "()")
        assertJavascriptNamingPresent("function2", "()")
    }

    @Test
    void withArgs() {
        assertJavascriptNamingPresent("function3", "(param1,param2)")
    }

    @Test
    void generatorDeclarationNoArg() {
        assertJavascriptNamingPresent("function4", "()")
    }

    @Test
    void generatorDeclarationWithArg() {
        assertJavascriptNamingPresent("function5", "(param)")
    }

    @Test
    void defaultParam() {
        assertJavascriptNamingPresent("function6", "(param)")
    }

    @Test
    void restOperatorParam() {
        assertJavascriptNamingPresent("function7", "(param1,param2)")
    }

    @Test
    void withTypedArg() {
        assertJavascriptNamingPresent("function8", "(param1,param2)")
    }

    private static void assertJavascriptNamingPresent(String functionName, String argsList) {
        def file = new File("src/test/resources/same/functions/Functions.js")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.babelfishName)

        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter(functionName + argsList)
        boolean foundFunction = false
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals(functionName + argsList, it.name)
            foundFunction = true
        }
        assertTrue(foundFunction)
    }
}
