package com.codebrig.arthur.observe.structure.naming

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.FunctionFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.assertTrue

class JavascriptNamingTest extends ArthurTest {

    private static final String functionsFile = "src/test/resources/same/functions/Functions.js"
    private static final String anonymousFolder = "src/test/resources/same/functions/javascript/anonymous"

    @Test
    void noArgs() {
        assertJavascriptNamingPresent("function1()", new File(functionsFile))
        assertJavascriptNamingPresent("function2()", new File(functionsFile))
    }

    @Test
    void withArg() {
        assertJavascriptNamingPresent("function3()", new File(functionsFile))
    }

    @Test
    void anonymousNoArg() {
        assertJavascriptNamingPresent("()", new File("${anonymousFolder}/AnonymousNoArg.js"))
    }

    @Test
    void anonymousWithArg() {
        assertJavascriptNamingPresent("()", new File("${anonymousFolder}/AnonymousWithArg.js"))
    }

    @Test
    void namedNoArg() {
        assertJavascriptNamingPresent("function4()", new File(functionsFile))
    }

    @Test
    void namedWithArg() {
        assertJavascriptNamingPresent("function5()", new File(functionsFile))
    }

    @Test
    void anonymousArrowNoArg() {
        assertJavascriptNamingPresent("()", new File("${anonymousFolder}/AnonymousArrowNoArg.js"))
    }

    @Test
    void anonymousArrowWithArg() {
        assertJavascriptNamingPresent("()", new File("${anonymousFolder}/AnonymousArrowWithArg.js"))
    }

    @Test
    void shorthandNoArg() {
        assertJavascriptNamingPresent("function6()", new File(functionsFile))
    }

    @Test
    void computedNoArg() {
        assertJavascriptNamingPresent("function7Var()", new File(functionsFile))
    }

    @Test
    void computedWithArg() {
        assertJavascriptNamingPresent("function8Var()", new File(functionsFile))
    }

    @Test
    void generatorDeclarationNoArg() {
        assertJavascriptNamingPresent("function9()", new File(functionsFile))
    }

    @Test
    void generatorDeclarationWithArg() {
        assertJavascriptNamingPresent("function10()", new File(functionsFile))
    }

    @Test
    void anonymousGeneratorExpressionNoArg() {
        assertJavascriptNamingPresent("()", new File("${anonymousFolder}/AnonymousGeneratorExpressionNoArg.js"))
    }

    @Test
    void anonymousGeneratorExpressionWithArg() {
        assertJavascriptNamingPresent("()", new File("${anonymousFolder}/AnonymousGeneratorExpressionWithArg.js"))
    }

    @Test
    void generatorExpressionNamedNoArg() {
        assertJavascriptNamingPresent("function11()", new File(functionsFile))
    }

    @Test
    void generatorExpressionNamedWithArg() {
        assertJavascriptNamingPresent("function12()", new File(functionsFile))
    }

    @Test
    void generatorShorthandNoArg() {
        assertJavascriptNamingPresent("function13()", new File(functionsFile))
    }

    @Test
    void generatorShorthandWithArg() {
        assertJavascriptNamingPresent("function14()", new File(functionsFile))
    }

    @Test
    void newFunction() {
        assertJavascriptNamingPresent("Function()", new File(functionsFile))
    }

    @Test
    void iife() {
        assertJavascriptNamingPresent("()", new File("${anonymousFolder}/AnonymousIIFE.js"))
    }

    @Test
    void defaultParam() {
        assertJavascriptNamingPresent("function15()", new File(functionsFile))
    }

    private static void assertJavascriptNamingPresent(String functionName, File file) {
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
