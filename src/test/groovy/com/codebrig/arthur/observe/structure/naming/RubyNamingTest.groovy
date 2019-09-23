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

class RubyNamingTest extends ArthurTest {

    @Test
    void noArgs() {
        assertRubyNamingPresent("function1", "()")
        assertRubyNamingPresent("function2", "()")
    }

    @Test
    void withArgs() {
        assertRubyNamingPresent("function3", "(param1,param2)")
    }

    @Test
    void varargs() {
        assertRubyNamingPresent("function4", "(param)")
    }

    @Test
    void ampersandArg() {
        assertRubyNamingPresent("function5", "(param)")
    }

    @Test
    void keywordArg1() {
        assertRubyNamingPresent("function6", "(param)")
    }

    @Test
    void keywordArg2() {
        assertRubyNamingPresent("function7", "(param)")
    }

    @Test
    void keywordArg3() {
        assertRubyNamingPresent("function8", "(param)")
    }

    private static void assertRubyNamingPresent(String functionName, String argsList) {
        def file = new File("src/test/resources/same/functions/Functions.rb")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.babelfishName, Encoding.UTF8$.MODULE$)

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
