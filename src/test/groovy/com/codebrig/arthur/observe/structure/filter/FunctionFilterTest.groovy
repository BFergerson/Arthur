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

    private static void assertFunctionsPresent(File file) {
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        boolean foundFunction = false
        new FunctionFilter().getFilteredNodes(language, resp.uast).each {
            def matched = MultiFilter.matchAll(
                    new TypeFilter("FunctionDeclaration", "FuncDecl", "FunctionDef",
                            "MethodDeclaration", "Stmt_Function", "def")
            ).getFilteredNodes(it)
            foundFunction = matched.hasNext()
        }
        assertTrue(foundFunction)
    }
}