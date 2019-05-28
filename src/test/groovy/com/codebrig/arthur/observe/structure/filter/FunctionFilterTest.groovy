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
        String anonymousFolder = "src/test/resources/same/functions/javascript/anonymous"
        assertFunctionsPresent(new File("src/test/resources/same/functions/Functions.js"))
        assertFunctionsPresent(new File("${anonymousFolder}/AnonymousNoArg.js"))
        assertFunctionsPresent(new File("${anonymousFolder}/AnonymousWithArg.js"))
        assertFunctionsPresent(new File("${anonymousFolder}/AnonymousArrowNoArg.js"))
        assertFunctionsPresent(new File("${anonymousFolder}/AnonymousArrowWithArg.js"))
        assertFunctionsPresent(new File("${anonymousFolder}/AnonymousGeneratorExpressionNoArg.js"))
        assertFunctionsPresent(new File("${anonymousFolder}/AnonymousGeneratorExpressionWithArg.js"))
        assertFunctionsPresent(new File("${anonymousFolder}/AnonymousIIFE.js"))
    }

    @Test
    void onlyFunctions_Go() {
        assertFunctionsPresent(new File("src/test/resources/same/functions/Functions.go"))
    }

    @Test
    void onlyFunctions_Php() {
        String anonymousFolder = "src/test/resources/same/functions/php/anonymous"
        assertFunctionsPresent(new File("src/test/resources/same/functions/Functions.php"))
        assertFunctionsPresent(new File("${anonymousFolder}/AnonymousNoArg.php"))
        assertFunctionsPresent(new File("${anonymousFolder}/AnonymousWithArg.php"))
        assertFunctionsPresent(new File("${anonymousFolder}/AnonymousInheritArg.php"))
        assertFunctionsPresent(new File("${anonymousFolder}/AnonymousInheritByReferenceArg.php"))
        assertFunctionsPresent(new File("${anonymousFolder}/AnonymousStaticNoArg.php"))
        assertFunctionsPresent(new File("${anonymousFolder}/AnonymousStaticWithArg.php"))
        assertFunctionsPresent(new File("${anonymousFolder}/AnonymousAutomaticBindThis.php"))
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
            foundFunction = true
        }
        assertTrue(foundFunction)
    }
}