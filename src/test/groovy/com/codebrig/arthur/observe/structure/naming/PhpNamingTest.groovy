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

class PhpNamingTest extends ArthurTest {

    @Test
    void noArgs() {
        assertPhpNamingPresent("function1", "()")
        assertPhpNamingPresent("function2", "()")
    }

    @Test
    void withArgs() {
        assertPhpNamingPresent("function3", "(param1,param2)")
    }

    @Test
    void defaultParam() {
        assertPhpNamingPresent("function4", "(param)")
    }

    @Test
    void defaultArrayParam() {
        assertPhpNamingPresent("function5", "(param)")
    }

    @Test
    void defaultNullParam() {
        assertPhpNamingPresent("function6", "(param)")
    }

    @Test
    void typedIntArg() {
        assertPhpNamingPresent("function7", "(param)")
    }

    @Test
    void passByReference() {
        assertPhpNamingPresent("function8", "(param)")
    }

    @Test
    void splatOperatorArg() {
        assertPhpNamingPresent("function9", "(param1,param2)")
    }

    private static void assertPhpNamingPresent(String functionName, String argsList) {
        def file = new File("src/test/resources/same/functions/Functions.php")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter(functionName + argsList)
        boolean foundFunction = false
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals(functionName + argsList, it.name)
            foundFunction = true
        }
        assertTrue(foundFunction)
    }
}
