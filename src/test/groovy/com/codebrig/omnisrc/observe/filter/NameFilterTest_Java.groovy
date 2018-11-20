package com.codebrig.omnisrc.observe.filter

import com.codebrig.omnisrc.OmniSRCTest
import com.codebrig.omnisrc.SourceLanguage
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.assertEquals

class NameFilterTest_Java extends OmniSRCTest {

    @Test
    void simpleName() {
        def file = new File("src/test/resources/java/ImportQualifiedName.java")
        def resp = client.parse(file.name, file.text, SourceLanguage.Java.key, Encoding.UTF8$.MODULE$)
        def fileFilter = new NameFilter("arg")

        fileFilter.getFilteredNodes(SourceLanguage.Java, resp.uast).each {
            assertEquals("SingleVariableDeclaration", it.internalType)
        }
    }
}