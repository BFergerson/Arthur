package com.codebrig.arthur.observe.structure.naming

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.FunctionFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.assertTrue

class JavascriptNamingTest extends ArthurTest {

    @Test
    void noArgs() {
        assertJavascriptNamingPresent("function1()")
        assertJavascriptNamingPresent("function2()")
    }

    @Test
    void withArg() {
        assertJavascriptNamingPresent("function3()")
    }

    @Test
    void namedNoArg() {
        assertJavascriptNamingPresent("function4()")
    }

    @Test
    void namedWithArg() {
        assertJavascriptNamingPresent("function5()")
    }

    @Test
    void shorthandNoArg() {
        assertJavascriptNamingPresent("function6()")
    }

    @Test
    void computedNoArg() {
        assertJavascriptNamingPresent("function7Var()")
    }

    @Test
    void computedWithArg() {
        assertJavascriptNamingPresent("function8Var()")
    }

    @Test
    void generatorDeclarationNoArg() {
        assertJavascriptNamingPresent("function9()")
    }

    @Test
    void generatorDeclarationWithArg() {
        assertJavascriptNamingPresent("function10()")
    }

    @Test
    void generatorExpressionNamedNoArg() {
        assertJavascriptNamingPresent("function11()")
    }

    @Test
    void generatorExpressionNamedWithArg() {
        assertJavascriptNamingPresent("function12()")
    }

    @Test
    void generatorShorthandNoArg() {
        assertJavascriptNamingPresent("function13()")
    }

    @Test
    void generatorShorthandWithArg() {
        assertJavascriptNamingPresent("function14()")
    }

    @Test
    void newFunction() {
        assertJavascriptNamingPresent("Function()")
    }

    @Test
    void defaultParam() {
        assertJavascriptNamingPresent("function15()")
    }

    private static void assertJavascriptNamingPresent(String functionName) {
        def file = new File("src/test/resources/same/functions/Functions.js")
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
