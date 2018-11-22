package com.codebrig.omnisrc.observe.filter.exception

import com.codebrig.omnisrc.OmniSRCTest
import com.codebrig.omnisrc.SourceLanguage
import com.codebrig.omnisrc.observe.filter.FunctionFilter
import com.codebrig.omnisrc.observe.filter.MultiFilter
import com.codebrig.omnisrc.observe.filter.NameFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.*

class TryFilterTest extends OmniSRCTest {

    @Test
    void tryCatch_Java() {
        def file = new File("src/test/resources/same/exceptions/Exceptions.java")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundTry = false
        def foundCatch = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("tryCatch")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("Exceptions.tryCatch()", it.name)

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

    @Test
    void tryCatchFinally_Java() {
        def file = new File("src/test/resources/same/exceptions/Exceptions.java")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundTry = false
        def foundCatch = false
        def foundFinally = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("tryCatchFinally")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("Exceptions.tryCatchFinally()", it.name)

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

    @Test
    void tryCatch_Javascript() {
        def file = new File("src/test/resources/same/exceptions/Exceptions.js")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundTry = false
        def foundCatch = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("tryCatch")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("tryCatch()", it.name)

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

    @Test
    void tryCatchFinally_Javascript() {
        def file = new File("src/test/resources/same/exceptions/Exceptions.js")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundTry = false
        def foundCatch = false
        def foundFinally = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("tryCatchFinally")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("tryCatchFinally()", it.name)

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

    @Test
    void tryCatch_Python() {
        def file = new File("src/test/resources/same/exceptions/Exceptions.py")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundTry = false
        def foundCatch = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("tryCatch")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("tryCatch()", it.name)

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

    @Test
    void tryCatchFinally_Python() {
        def file = new File("src/test/resources/same/exceptions/Exceptions.py")
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

        def foundTry = false
        def foundCatch = false
        def foundFinally = false
        def functionFilter = new FunctionFilter()
        def nameFilter = new NameFilter("tryCatchFinally")
        MultiFilter.matchAll(functionFilter, nameFilter).getFilteredNodes(language, resp.uast).each {
            assertEquals("tryCatchFinally()", it.name)

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