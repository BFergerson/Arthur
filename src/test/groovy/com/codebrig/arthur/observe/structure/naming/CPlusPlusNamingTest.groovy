package com.codebrig.arthur.observe.structure.naming

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.FunctionFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.NameFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.*

class CPlusPlusNamingTest extends ArthurTest {

    @Test
    void noArgs() {
        assertCPlusPlusNamingPresent("function1", "()")
    }

    @Test
    void withIntArg() {
        assertCPlusPlusNamingPresent("function2", "(int)")
    }

    @Test
    void withArgs() {
        assertCPlusPlusNamingPresent("function3", "(int,int)")
    }

    @Test
    void withVarargs1() {
        assertCPlusPlusNamingPresent("function4", "(int)")
    }

    @Test
    void withVarargs2() {
        assertCPlusPlusNamingPresent("function5", "(int)")
    }

    @Test
    void withVarargs3() {
        assertCPlusPlusNamingPresent("function6", "(int)")
    }

    @Test
    void withVarargs4() {
        assertCPlusPlusNamingPresent("function7", "(int)")
    }

    @Test
    void withVarargs5() {
        assertCPlusPlusNamingPresent("function8", "(int,string,int)")
    }

    @Test
    void withArrayArg1() {
        assertCPlusPlusNamingPresent("function9", "(int)")
    }

    @Test
    void withArrayArg2() {
        assertCPlusPlusNamingPresent("function10", "(int)")
    }

    @Test
    void withArrayArg3() {
        assertCPlusPlusNamingPresent("function11", "(int,string)")
    }

    @Test
    void withArrayArg4() {
        assertCPlusPlusNamingPresent("function12", "(string,int,int)")
    }

    @Test
    void withGenericsArg() {
        assertCPlusPlusNamingPresent("function13", "(T)")
    }

    @Test
    void withGenericsListArg1() {
        assertCPlusPlusNamingPresent("function14", "(list<T>)")
    }

    @Test
    void withGenericsListArg2() {
        assertCPlusPlusNamingPresent("function15", "(string,list<T>)")
    }

    @Test
    void withStructArg() {
        assertCPlusPlusNamingPresent("function16", "(myStruct)")
    }

    private static void assertCPlusPlusNamingPresent(String functionName, String argsList) {
        def file = new File("src/test/resources/same/functions/Functions.cpp")
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
