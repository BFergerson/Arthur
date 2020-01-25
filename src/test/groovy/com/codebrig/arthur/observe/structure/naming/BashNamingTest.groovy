package com.codebrig.arthur.observe.structure.naming

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.FunctionFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.NameFilter
import org.bblfsh.client.v2.BblfshClient
import org.junit.Test

import static org.junit.Assert.*

class BashNamingTest extends ArthurTest {

    @Test
    void notUsingArgs() {
        assertBashNamingPresent("function1", "()")
        assertBashNamingPresent("function2", "()")
    }

    @Test
    void usingArgs() {
        assertBashNamingPresent("function3", "()")
        assertBashNamingPresent("function4", "()")
        assertBashNamingPresent("function5", "()")
    }

    private static void assertBashNamingPresent(String functionName, String argsList) {
        def file = new File("src/test/resources/same/functions/Functions.sh")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.babelfishName)
        def rootNode = new BblfshClient.UastMethods(resp.uast()).decode().root().load()

        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter(functionName + argsList)
        boolean foundFunction = false
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, rootNode).each {
            assertEquals(functionName + argsList, it.name)
            foundFunction = true
        }
        assertTrue(foundFunction)
    }
}
