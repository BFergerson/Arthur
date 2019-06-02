package com.codebrig.arthur.observe.structure.naming

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.FunctionFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.assertTrue

class RubyNamingTest extends ArthurTest {

    @Test
    void noArgs() {
        assertRubyNamingPresent("function1()")
        assertRubyNamingPresent("function2()")
    }

    @Test
    void withArg() {
        assertRubyNamingPresent("function3()")
    }

    @Test
    void varargs() {
        assertRubyNamingPresent("function4()")
    }

    @Test
    void ampersandArg() {
        assertRubyNamingPresent("function5()")
    }

    @Test
    void keywordArg1() {
        assertRubyNamingPresent("function6()")
    }

    @Test
    void keywordArg2() {
        assertRubyNamingPresent("function7()")
    }

    @Test
    void keywordArg3() {
        assertRubyNamingPresent("function8()")
    }

    @Test
    void dynamicMethod() {
        assertRubyNamingPresent("function9()")
    }

    private static void assertRubyNamingPresent(String functionName) {
        def file = new File("src/test/resources/same/functions/Functions.rb")
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
