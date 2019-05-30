package com.codebrig.arthur.observe.structure.naming

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.FunctionFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.assertTrue

class PythonNamingTest extends ArthurTest {

    @Test
    void noArgs() {
        assertPythonNamingPresent("function1()")
        assertPythonNamingPresent("function2()")
    }

    @Test
    void withArg() {
        assertPythonNamingPresent("function3()")
    }

    @Test
    void defaultArg() {
        assertPythonNamingPresent("function4()")
    }

    /*
    @Test
    void variadicArgs() {
        assertPythonNamingPresent("function5()")
    }
    */

    @Test
    void keywordArgs() {
        assertPythonNamingPresent("function6()")
    }

    /*
    @Test
    void variadicAndKeywordArgs() {
        assertPythonNamingPresent("function7()")
    }
    */

    /*
    @Test
    void functionAnnotation() {
        assertPythonNamingPresent("function8()")
    }
    */

    private static void assertPythonNamingPresent(String functionName) {
        def file = new File("src/test/resources/same/functions/Functions.py")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def functionFilter = new FunctionFilter()
        boolean foundFunction = false
        MultiFilter.matchAll(functionFilter).getFilteredNodes(language, resp.uast).each {
            if (functionName == it.name) {
                foundFunction = true
            }
        }
        assertTrue(foundFunction)
    }
}
