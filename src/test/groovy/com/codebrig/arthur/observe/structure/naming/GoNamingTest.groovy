package com.codebrig.arthur.observe.structure.naming

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.FunctionFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.NameFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.*

class GoNamingTest extends ArthurTest {

    @Test
    void noArgs() {
        assertGoNamingPresent("function1", "()")
        assertGoNamingPresent("function2", "()")
    }

    @Test
    void uint64Arg() {
        assertGoNamingPresent("function3", "(uint64)")
    }

    @Test
    void variadicFunc() {
        assertGoNamingPresent("function4", "(uint32,...uint64)")
    }

    @Test
    void uint64ArrayArg() {
        assertGoNamingPresent("function5", "([3]uint64,...uint64)")
    }

    @Test
    void uint64SliceArg() {
        assertGoNamingPresent("function6", "([]uint64)")
    }

    @Test
    void exportedFunc() {
        assertGoNamingPresent("Function7", "(uint64)")
    }

    @Test
    void asMethodWithReceiver1() {
        assertGoNamingPresent("function8", "(uint64)")
    }

    @Test
    void asMethodWithReceiver2() {
        assertGoNamingPresent("function9", "(uint64)")
    }

    @Test
    void pointerArg() {
        assertGoNamingPresent("function10", "(*uint64)")
    }

    @Test
    void asMethodWithPointerReceiverArg1() {
        assertGoNamingPresent("function11", "(uint64)")
    }

    @Test
    void asMethodWithPointerReceiverArg2() {
        assertGoNamingPresent("function12", "(uint64)")
    }

    private static void assertGoNamingPresent(String functionName, String argsList) {
        def file = new File("src/test/resources/same/functions/Functions.go")
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
