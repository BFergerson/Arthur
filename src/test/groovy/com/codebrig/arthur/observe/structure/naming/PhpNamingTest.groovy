package com.codebrig.arthur.observe.structure.naming

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.FunctionFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.assertTrue

class PhpNamingTest extends ArthurTest {

    @Test
    void noArgs() {
        assertPhpNamingPresent("function1()")
        assertPhpNamingPresent("function2()")
    }

    @Test
    void withArg() {
        assertPhpNamingPresent("function3(param1,param2)")
    }

    @Test
    void defaultParam() {
        assertPhpNamingPresent("function4(param)")
    }

    @Test
    void defaultArrayParam() {
        assertPhpNamingPresent("function5(param)")
    }

    @Test
    void defaultNullParam() {
        assertPhpNamingPresent("function6(param)")
    }

    @Test
    void typedIntArg() {
        assertPhpNamingPresent("function7(param)")
    }

    @Test
    void passByReference() {
        assertPhpNamingPresent("function8(param)")
    }

    @Test
    void splatOperatorArg() {
        assertPhpNamingPresent("function9(param1,param2)")
    }

    private static void assertPhpNamingPresent(String functionName) {
        def file = new File("src/test/resources/same/functions/Functions.php")
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
