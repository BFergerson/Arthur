package com.codebrig.arthur.observe.structure.filter

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

class LambdaExpressionTest_Java extends ArthurTest {

    @Test
    void onlyFunctions_Java() {
        assertFunctionsPresent(new File("src/test/resources/java/LambdaExpression.java"))
    }

    private static void assertFunctionsPresent(File file) {
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.babelfishName, Encoding.UTF8$.MODULE$)

        int foundFunctions = 0
        new FunctionFilter().getFilteredNodes(language, resp.uast).each {
            assertTrue(it.hasName())
            foundFunctions++
        }
        assertEquals(2, foundFunctions)
    }
}
