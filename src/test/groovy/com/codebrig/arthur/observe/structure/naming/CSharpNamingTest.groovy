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

class CSharpNamingTest extends ArthurTest {

    @Test
    void noArgs() {
        assertCSharpNamingPresent("Function1", "()")
    }

    @Test
    void withIntArg() {
        assertCSharpNamingPresent("Function2", "(int)")
    }

    @Test
    void withArgs() {
        assertCSharpNamingPresent("Function3", "(int,string)")
    }

    @Test
    void intVarargs() {
        assertCSharpNamingPresent("Function4", "(int)")
    }

    @Test
    void intArrayArg() {
        assertCSharpNamingPresent("Function5", "(int)")
    }

    @Test
    void signedArgs() {
        assertCSharpNamingPresent("Function6", "(sbyte,short,long)")
    }

    @Test
    void unsignedArgs() {
        assertCSharpNamingPresent("Function7", "(byte,ushort,uint,ulong)")
    }

    @Test
    void restOfSimpleArgs() {
        assertCSharpNamingPresent("Function8", "(bool,decimal,float,double)")
    }

    @Test
    void structArg() {
        assertCSharpNamingPresent("Function9", "(MyStruct)")
    }

    @Test
    void enumArg() {
        assertCSharpNamingPresent("Function10", "(Day)")
    }

    @Test
    void genericsType1Arg() {
        assertCSharpNamingPresent("Function11", "(T)")
    }

    @Test
    void genericsType2Arg() {
        assertCSharpNamingPresent("Function12", "(List<T>)")
    }

    @Test
    void genericsObjectArg() {
        assertCSharpNamingPresent("Function13", "(List<Day>)")
        assertCSharpNamingPresent("Function14", "(List<MyStruct>)")
    }

    @Test
    void genericsStructArgAndIntVarargs() {
        assertCSharpNamingPresent("Function15", "(List<MyStruct>,int)")
    }

    private static void assertCSharpNamingPresent(String functionName, String argsList) {
        def file = new File("src/test/resources/same/functions/Functions.cs")
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
