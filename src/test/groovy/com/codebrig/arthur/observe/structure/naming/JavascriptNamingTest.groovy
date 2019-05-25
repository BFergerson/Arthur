package com.codebrig.arthur.observe.structure.naming

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.FunctionFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.TypeFilter
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
    void expressionNoArg() {
        assertJavascriptNamingPresent("\"var function4 = function()\"")
    }

    @Test
    void expressionWithArg() {
        assertJavascriptNamingPresent("\"var function5 = function(param)\"")
    }

    @Test
    void arrowFunction() {
        assertJavascriptNamingPresent("\"var function6 = (param)\"")
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
            } else {
                MultiFilter.matchAll(
                        new TypeFilter("FunctionExpression", "ArrowFunctionExpression")
                ).getFilteredNodes(
                    MultiFilter.matchAll(
                            new TypeFilter("BlockExpression")
                    ).getFilteredNodes(
                        MultiFilter.matchAll(
                                new TypeFilter("ExpressionStatement")
                        ).getFilteredNodes(
                                MultiFilter.matchAll(
                                        new TypeFilter("CallExpression")
                                ).getFilteredNodes(
                                        MultiFilter.matchAll(
                                            new TypeFilter("StringLiteral")
                                        ).getFilteredNodes(it).each {
                                            println ">>>>> it.token = ${it.token}"
                                            if (functionName == it.token) {
                                                foundFunction = true
                                            }
                                        })))
                )
            }
        }
        assertTrue(foundFunction)
    }
}
