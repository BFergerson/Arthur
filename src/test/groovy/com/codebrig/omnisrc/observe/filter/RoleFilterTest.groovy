package com.codebrig.omnisrc.observe.filter

import com.codebrig.omnisrc.OmniSRCTest
import com.codebrig.omnisrc.SourceLanguage
import com.codebrig.omnisrc.observe.filter.loop.ForLoopFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.assertTrue

class RoleFilterTest extends OmniSRCTest {

    @Test
    void onlyForStatementsFilter() {
        def filter = new RoleFilter("FOR_ITERATOR_STATEMENT", "FOR_STATEMENT")
        def parseFolder = new File("src/test/resources/same/program")
        parseFolder.listFiles().each { file ->
            def language = SourceLanguage.getSourceLanguage(file)
            def resp = client.parse(file.name, file.text, language.key, Encoding.UTF8$.MODULE$)

            boolean foundForStatement = false
            filter.getFilteredNodes(language, resp.uast).each {
                assertTrue(ForLoopFilter.LOOP_TYPES.contains(it.internalType))
                foundForStatement = true
            }
            assertTrue(foundForStatement)
        }
    }
}
