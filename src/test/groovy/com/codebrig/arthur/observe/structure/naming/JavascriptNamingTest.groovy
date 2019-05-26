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
    void anonymousNoArg() {
        assertJavascriptNamingPresent("()")
    }

    @Test
    void anonymousWithArg() {
        assertJavascriptNamingPresent("()")
    }

    @Test
    void namedNoArg() {
        assertJavascriptNamingPresent("function6()")
    }

    @Test
    void namedWithArg() {
        assertJavascriptNamingPresent("function7()")
    }

    @Test
    void anonymousArrowNoArg() {
        assertJavascriptNamingPresent("()")
    }

    @Test
    void anonymousArrowWithArg() {
        assertJavascriptNamingPresent("()")
    }

    @Test
    void shorthandNoArg() {
        assertJavascriptNamingPresent("function10()")
    }

    @Test
    void computedNoArg() {
        assertJavascriptNamingPresent("function11Var()")
    }

    @Test
    void computedWithArg() {
        assertJavascriptNamingPresent("function12Var()")
    }

    @Test
    void generatorDeclarationNoArg() {
        assertJavascriptNamingPresent("function13()")
    }

    @Test
    void generatorDeclarationWithArg() {
        assertJavascriptNamingPresent("function14()")
    }

    @Test
    void generatorExpressionAnonymousNoArg() {
        assertJavascriptNamingPresent("()")
    }

    @Test
    void generatorExpressionAnonymousWithArg() {
        assertJavascriptNamingPresent("()")
    }

    @Test
    void generatorExpressionNamedNoArg() {
        assertJavascriptNamingPresent("function17()")
    }

    @Test
    void generatorExpressionNamedWithArg() {
        assertJavascriptNamingPresent("function18()")
    }

    @Test
    void generatorShorthandNoArg() {
        assertJavascriptNamingPresent("function19()")
    }

    @Test
    void generatorShorthandWithArg() {
        assertJavascriptNamingPresent("function20()")
    }

    @Test
    void newFunction() {
        assertJavascriptNamingPresent("Function()")
    }

    @Test
    void iife() {
        assertJavascriptNamingPresent("()")
    }

    @Test
    void defaultParam() {
        assertJavascriptNamingPresent("function21()")
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
