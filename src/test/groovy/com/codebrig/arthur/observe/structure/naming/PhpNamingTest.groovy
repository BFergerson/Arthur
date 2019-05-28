package com.codebrig.arthur.observe.structure.naming

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.FunctionFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.assertTrue

class PhpNamingTest extends ArthurTest {

    private static final String functionsFile = "src/test/resources/same/functions/Functions.php"
    private static final String anonymousFolder = "src/test/resources/same/functions/php/anonymous"

    @Test
    void noArgs() {
        assertPhpNamingPresent("function1()", new File(functionsFile))
        assertPhpNamingPresent("function2()", new File(functionsFile))
    }

    @Test
    void withArg() {
        assertPhpNamingPresent("function3()", new File(functionsFile))
    }

    @Test
    void defaultParam() {
        assertPhpNamingPresent("function4()", new File(functionsFile))
    }

    @Test
    void defaultArrayParam() {
        assertPhpNamingPresent("function5()", new File(functionsFile))
    }

    @Test
    void defaultNullParam() {
        assertPhpNamingPresent("function6()", new File(functionsFile))
    }

    @Test
    void typedIntArg() {
        assertPhpNamingPresent("function7()", new File(functionsFile))
    }

    @Test
    void passByReference() {
        assertPhpNamingPresent("function8()", new File(functionsFile))
    }

    @Test
    void anonymousNoArg() {
        assertPhpNamingPresent("()", new File("${anonymousFolder}/AnonymousNoArg.php"))
    }

    @Test
    void anonymousWithArg() {
        assertPhpNamingPresent("()", new File("${anonymousFolder}/AnonymousWithArg.php"))
    }

    @Test
    void anonymousInheritArg() {
        assertPhpNamingPresent("()", new File("${anonymousFolder}/AnonymousInheritArg.php"))
    }

    @Test
    void anonymousInheritByReferenceArg() {
        assertPhpNamingPresent("()", new File("${anonymousFolder}/AnonymousInheritByReferenceArg.php"))
    }

    @Test
    void anonymousStaticNoArg() {
        assertPhpNamingPresent("()", new File("${anonymousFolder}/AnonymousStaticNoArg.php"))
    }

    @Test
    void anonymousStaticWithArg() {
        assertPhpNamingPresent("()", new File("${anonymousFolder}/AnonymousStaticWithArg.php"))
    }

    @Test
    void anonymousAutomaticBindThis() {
        assertPhpNamingPresent("()", new File("${anonymousFolder}/AnonymousAutomaticBindThis.php"))
    }

    private static void assertPhpNamingPresent(String functionName, File file) {
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
