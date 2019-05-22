package com.codebrig.arthur.observe.structure.naming

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.FunctionFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.NameFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.*

class JavaNamingTest extends ArthurTest {

    @Test
    void noArgs() {
        assertJavaNamingPresent("function1", "()")
        assertJavaNamingPresent("function2", "()")
    }

    @Test
    void intArg() {
        assertJavaNamingPresent("function3", "(int)")
    }

    @Test
    void intVarargs() {
        assertJavaNamingPresent("function4", "(int)")
    }

    @Test
    void intArray1Arg() {
        assertJavaNamingPresent("function5", "(int)")
    }

    @Test
    void intArray2Arg() {
        assertJavaNamingPresent("function6", "(int[])")
    }

    @Test
    void integerArrayArg() {
        assertJavaNamingPresent("function7", "(java.lang.Integer[])")
    }

    @Test
    void genericsType1Arg() {
        assertJavaNamingPresent("function8", "(T)")
    }

    @Test
    void genericsType2Arg() {
        assertJavaNamingPresent("function9", "(List<T>)")
    }

    @Test
    void genericsIntegerArg() {
        assertJavaNamingPresent("function10", "(List<java.lang.Integer>)")
    }

    private static void assertJavaNamingPresent(String functionName, String argsList) {
        def file = new File("src/test/resources/same/functions/Functions.java")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter(functionName)
        boolean foundFunction = false
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("Functions." + functionName + argsList, it.name)
            foundFunction = true
        }
        assertTrue(foundFunction)
    }
}
