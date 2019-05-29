package com.codebrig.arthur.observe.structure.naming

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.FunctionFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.assertTrue

class PythonNamingTest extends ArthurTest {

    private static final String functionsFile = "src/test/resources/same/functions/Functions.py"

    @Test
    void noArgs() {
        assertPythonNamingPresent("function1()", new File(functionsFile))
        assertPythonNamingPresent("function2()", new File(functionsFile))
    }

    @Test
    void withArg() {
        assertPythonNamingPresent("function3()", new File(functionsFile))
    }

    @Test
    void defaultArg() {
        assertPythonNamingPresent("function4()", new File(functionsFile))
    }

    /*
    @Test
    void variadicArg() {
        assertPythonNamingPresent("function5()", new File(functionsFile))
    }
    */

    private static void assertPythonNamingPresent(String functionName, File file) {
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
