package com.codebrig.arthur.observe.structure.naming

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.FunctionFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.NameFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

class PythonNamingTest extends ArthurTest {

    @Test
    void noArgs() {
        assertPythonNamingPresent("function1", "()")
        assertPythonNamingPresent("function2", "()")
    }

    @Test
    void withArgs() {
        assertPythonNamingPresent("function3", "(param1,param2)")
    }

    @Test
    void defaultArg() {
        assertPythonNamingPresent("function4", "(param)")
    }

    @Test
    void keywordArgs() {
        assertPythonNamingPresent("function5", "(param)")
    }

    /*
     * Exception in variadic arguments
     * https://github.com/bblfsh/python-driver/issues/199
     *
    @Test
    void variadicArgs() {
        assertPythonNamingPresent("function6", "(param1,param2)")
    }

    @Test
    void variadicAndKeywordArgs() {
        assertPythonNamingPresent("function7", "(param,args,kwargs)")
    }
    */

    /*
     * Exception in function annotations
     * https://github.com/bblfsh/python-driver/issues/200
     *
    @Test
    void functionAnnotation() {
        assertPythonNamingPresent("function8", "(param)")
    }
    */

    /*
     * Inconsistent ordering of arguments if one is a keyword argument
     * https://github.com/bblfsh/python-driver/issues/201
     *
    @Test
    void keywordArgs() {
        assertPythonNamingPresent("function9", "(param1,param2)")
    }
    */

    private static void assertPythonNamingPresent(String functionName, String argsList) {
        def file = new File("src/test/resources/same/functions/Functions.py")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter(functionName)
        boolean foundFunction = false
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals(functionName + argsList, it.name)
            foundFunction = true
        }
        assertTrue(foundFunction)
    }
}
