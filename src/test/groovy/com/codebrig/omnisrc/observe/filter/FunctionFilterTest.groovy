package com.codebrig.omnisrc.observe.filter

import com.codebrig.omnisrc.OmniSRCTest
import com.codebrig.omnisrc.SourceLanguage
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

class FunctionFilterTest extends OmniSRCTest {

    @Test
    void onlyFunctions_Java() {
        def filter = new FunctionFilter()
        def file = new File("src/test/resources/same/functions/Functions.java")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        boolean foundFunction1 = false
        boolean foundFunction2 = false
        filter.getFilteredNodes(language, resp.uast).each {
            if (!foundFunction1) {
                assertEquals("Operators.function1()", it.name)
                foundFunction1 = true
            } else {
                assertEquals("Operators.function2()", it.name)
                foundFunction2 = true
            }
        }
        assertTrue(foundFunction1)
        assertTrue(foundFunction2)
    }

    @Test
    void onlyFunctions_Javascript() {
        def filter = new FunctionFilter()
        def file = new File("src/test/resources/same/functions/Functions.js")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        boolean foundFunction1 = false
        boolean foundFunction2 = false
        filter.getFilteredNodes(language, resp.uast).each {
            if (!foundFunction1) {
                assertEquals("function1()", it.name)
                foundFunction1 = true
            } else {
                assertEquals("function2()", it.name)
                foundFunction2 = true
            }
        }
        assertTrue(foundFunction1)
        assertTrue(foundFunction2)
    }

    @Test
    void onlyFunctions_Go() {
        def filter = new FunctionFilter()
        def file = new File("src/test/resources/same/functions/Functions.go")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        boolean foundFunction1 = false
        boolean foundFunction2 = false
        filter.getFilteredNodes(language, resp.uast).each {
            if (!foundFunction1) {
                assertEquals("function1()", it.name)
                foundFunction1 = true
            } else {
                assertEquals("function2()", it.name)
                foundFunction2 = true
            }
        }
        assertTrue(foundFunction1)
        assertTrue(foundFunction2)
    }

    @Test
    void onlyFunctions_Python() {
        def filter = new FunctionFilter()
        def file = new File("src/test/resources/same/functions/Functions.py")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        boolean foundFunction1 = false
        boolean foundFunction2 = false
        filter.getFilteredNodes(language, resp.uast).each {
            if (!foundFunction1) {
                assertEquals("function1()", it.name)
                foundFunction1 = true
            } else {
                assertEquals("function2()", it.name)
                foundFunction2 = true
            }
        }
        assertTrue(foundFunction1)
        assertTrue(foundFunction2)
    }
}