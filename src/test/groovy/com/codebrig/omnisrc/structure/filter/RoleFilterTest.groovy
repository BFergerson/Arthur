package com.codebrig.omnisrc.structure.filter

import com.codebrig.omnisrc.OmniSRCTest
import com.codebrig.omnisrc.SourceLanguage
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.assertTrue

class RoleFilterTest extends OmniSRCTest {

    @Test
    void onlyForStatementsFilter() {
        def filter = new RoleFilter("FOR_ITERATOR_STATEMENT", "FOR_STATEMENT")
        def parseFolder = new File("src/test/resources/same/")
        parseFolder.listFiles().each { file ->
            def resp = client.parse(file.name, file.text, SourceLanguage.getSourceLanguage(file).key(), Encoding.UTF8$.MODULE$)

            boolean foundForStatement = false
            filter.getFilteredNodes(resp.uast).each {
                assertTrue(["For", "ForStmt", "ForStatement"].contains(it.internalType()))
                foundForStatement = true
            }
            assertTrue(foundForStatement)
        }
    }
}
