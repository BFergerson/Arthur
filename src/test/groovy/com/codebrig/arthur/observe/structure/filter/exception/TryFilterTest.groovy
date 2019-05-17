package com.codebrig.arthur.observe.structure.filter.exception

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.structure.filter.FunctionFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.NameFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.*

class TryFilterTest extends ArthurTest {

    @Test
    void tryCatch_Java() {
        assertTryCatchPresent(new File("src/test/resources/same/exceptions/Exceptions.java"),
                "Exceptions.")
    }

    @Test
    void tryCatchFinally_Java() {
        assertTryCatchFinallyPresent(new File("src/test/resources/same/exceptions/Exceptions.java"),
                "Exceptions.")
    }

    @Test
    void tryCatch_Javascript() {
        assertTryCatchPresent(new File("src/test/resources/same/exceptions/Exceptions.js"))
    }

    @Test
    void tryCatchFinally_Javascript() {
        assertTryCatchFinallyPresent(new File("src/test/resources/same/exceptions/Exceptions.js"))
    }

    @Test
    void tryCatch_Python() {
        assertTryCatchPresent(new File("src/test/resources/same/exceptions/Exceptions.py"))
    }

    @Test
    void tryCatchFinally_Python() {
        assertTryCatchFinallyPresent(new File("src/test/resources/same/exceptions/Exceptions.py"))
    }

    private static void assertTryCatchPresent(File file) {
        assertTryCatchPresent(file, "")
    }

    private static void assertTryCatchPresent(File file, String qualifiedName) {
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundTry = false
        def foundCatch = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("tryCatch")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals(qualifiedName + "tryCatch()", it.name)

            new TryFilter().getFilteredNodes(it).each {
                assertFalse(foundTry)
                foundTry = true
            }
            new CatchFilter().getFilteredNodes(it).each {
                assertFalse(foundCatch)
                foundCatch = true
            }
        }
        assertTrue(foundTry)
        assertTrue(foundCatch)
    }

    private static void assertTryCatchFinallyPresent(File file) {
        assertTryCatchFinallyPresent(file, "")
    }

    private static void assertTryCatchFinallyPresent(File file, String qualifiedName) {
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundTry = false
        def foundCatch = false
        def foundFinally = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("tryCatchFinally")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals(qualifiedName + "tryCatchFinally()", it.name)

            new TryFilter().getFilteredNodes(it).each {
                assertFalse(foundTry)
                foundTry = true
            }
            new CatchFilter().getFilteredNodes(it).each {
                assertFalse(foundCatch)
                foundCatch = true
            }
            new FinallyFilter().getFilteredNodes(it).each {
                assertFalse(foundFinally)
                foundFinally = true
            }
        }
        assertTrue(foundTry)
        assertTrue(foundCatch)
        assertTrue(foundFinally)
    }
}