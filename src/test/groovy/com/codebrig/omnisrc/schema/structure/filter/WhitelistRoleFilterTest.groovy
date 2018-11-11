package com.codebrig.omnisrc.schema.structure.filter

import com.codebrig.omnisrc.OmniSRCTest
import com.codebrig.omnisrc.SourceLanguage
import com.codebrig.omnisrc.schema.filter.WhitelistRoleFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.assertTrue

class WhitelistRoleFilterTest extends OmniSRCTest {

    @Test
    void onlyForStatementsFilter() {
        def filter = new WhitelistRoleFilter("FOR_ITERATOR_STATEMENT", "FOR_STATEMENT")
        def parseFolder = new File("src/test/resources/same/")
        parseFolder.listFiles().each { file ->
            def language = SourceLanguage.getSourceLanguage(file)
            def resp = client.parse(file.name, file.text, language.key(), Encoding.UTF8$.MODULE$)

            boolean foundForStatement = false
            filter.getFilteredNodes(language, resp.uast).each {
                assertTrue(["For", "ForStmt", "ForStatement"].contains(it.internalType))
                foundForStatement = true
            }
            assertTrue(foundForStatement)
        }
    }
}
