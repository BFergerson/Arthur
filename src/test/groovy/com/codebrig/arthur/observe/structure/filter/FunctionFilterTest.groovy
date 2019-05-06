package com.codebrig.arthur.observe.structure.filter

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

class FunctionFilterTest extends ArthurTest {

    @Test
    void onlyFunctions_Java() {
        assertFunctionsPresent(new File("src/test/resources/same/functions/Functions.java"),
                "Functions.")
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

    private static void assertFunctionsPresent(File file) {
        assertFunctionsPresent(file, "")
    }

    private static void assertFunctionsPresent(File file, String qualifiedName) {
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        boolean foundFunction1 = false
        boolean foundFunction2 = false
        new FunctionFilter().getFilteredNodes(language, resp.uast).each {
            if (!foundFunction1) {
                assertEquals(qualifiedName + "function1()", it.name)
                foundFunction1 = true
            } else {
                assertEquals(qualifiedName + "function2()", it.name)
                foundFunction2 = true
            }
        }
        assertTrue(foundFunction1)
        assertTrue(foundFunction2)
    }
}