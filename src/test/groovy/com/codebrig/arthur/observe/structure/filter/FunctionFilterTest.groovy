package com.codebrig.arthur.observe.structure.filter

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.assertTrue

class FunctionFilterTest extends ArthurTest {

    @Test
    void onlyFunctions_Java() {
        assertFunctionsPresent(new File("src/test/resources/same/functions/Functions.java"))
    }

    @Test
    void onlyFunctions_Javascript() {
        assertFunctionsPresent(new File("src/test/resources/same/functions/Functions.js"))
    }

    @Test
    void onlyFunctions_Go() {
        assertFunctionsPresent(new File("src/test/resources/same/functions/Functions.go"))
    }

    @Test
    void onlyFunctions_Php() {
        assertFunctionsPresent(new File("src/test/resources/same/functions/Functions.php"))
    }

    @Test
    void onlyFunctions_Python() {
        assertFunctionsPresent(new File("src/test/resources/same/functions/Functions.py"))
    }

    @Test
    void onlyFunctions_Ruby() {
        assertFunctionsPresent(new File("src/test/resources/same/functions/Functions.rb"))
    }

    @Test
    void onlyFunctions_CSharp() {
        assertFunctionsPresent(new File("src/test/resources/same/functions/Functions.cs"))
    }

    @Test
    void onlyFunctions_CPlusPlus() {
        assertFunctionsPresent(new File("src/test/resources/same/functions/Functions.cpp"))
    }

    @Test
    void onlyFunctions_Bash() {
        assertFunctionsPresent(new File("src/test/resources/same/functions/Functions.sh"))
    }

    private static void assertFunctionsPresent(File file) {
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.babelfishName, Encoding.UTF8$.MODULE$)

        boolean foundFunction = false
        new FunctionFilter().getFilteredNodes(language, resp.uast).each {
            foundFunction = true

            //verify functions are not found inside functions
            assertTrue(new FunctionFilter().getFilteredNodes(it).size() == 1)
        }
        assertTrue(foundFunction)
    }
}