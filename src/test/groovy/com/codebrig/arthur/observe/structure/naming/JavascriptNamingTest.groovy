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
    void namedNoArg() {
        assertJavascriptNamingPresent("function4", "()")
    }

    @Test
    void namedWithArg() {
        assertJavascriptNamingPresent("function5", "(param)")
    }

    @Test
    void shorthandNoArg() {
        assertJavascriptNamingPresent("function6", "()")
    }

    @Test
    void computedNoArg() {
        assertJavascriptNamingPresent("function7Var", "()")
    }

    @Test
    void computedWithArg() {
        assertJavascriptNamingPresent("function8Var", "(param)")
    }

    @Test
    void generatorDeclarationNoArg() {
        assertJavascriptNamingPresent("function9", "()")
    }

    @Test
    void generatorDeclarationWithArg() {
        assertJavascriptNamingPresent("function10", "(param)")
    }

    @Test
    void generatorExpressionNamedNoArg() {
        assertJavascriptNamingPresent("function11", "()")
    }

    @Test
    void generatorExpressionNamedWithArg() {
        assertJavascriptNamingPresent("function12", "(param)")
    }

    @Test
    void generatorShorthandNoArg() {
        assertJavascriptNamingPresent("function13", "()")
    }

    @Test
    void generatorShorthandWithArg() {
        assertJavascriptNamingPresent("function14", "(param)")
    }

    @Test
    void newFunction() {
        assertJavascriptNamingPresent("Function", "(number1,number2,'return number1 + number2')")
    }

    @Test
    void defaultParam() {
        assertJavascriptNamingPresent("function15", "(param)")
    }

    @Test
    void restOperatorParam() {
        assertJavascriptNamingPresent("function16", "(param1,param2)")
    }

    @Test
    void withTypedArg() {
        assertJavascriptNamingPresent("function17", "(param1,param2)")
    }

    private static void assertJavascriptNamingPresent(String functionName, String argsList) {
        def file = new File("src/test/resources/same/functions/Functions.js")
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
