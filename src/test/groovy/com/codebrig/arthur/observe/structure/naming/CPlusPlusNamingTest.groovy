package com.codebrig.arthur.observe.structure.naming

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.FunctionFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.NameFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.*

class CPlusPlusNamingTest extends ArthurTest {

    @Test
    void noArgs() {
        assertCPlusPlusNamingPresent("function1", "()")
    }

    @Test
    void withIntArg() {
        assertCPlusPlusNamingPresent("function2", "(int)")
    }

    @Test
    void withArgs() {
        assertCPlusPlusNamingPresent("function3", "(int,int)")
    }

    private static void assertCPlusPlusNamingPresent(String functionName, String argsList) {
        def file = new File("src/test/resources/same/functions/Functions.cpp")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.babelfishName, Encoding.UTF8$.MODULE$)

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
