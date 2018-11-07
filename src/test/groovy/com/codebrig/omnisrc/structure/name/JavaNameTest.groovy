package com.codebrig.omnisrc.structure.name

import com.codebrig.omnisrc.OmniSRCTest
import com.codebrig.omnisrc.SourceLanguage
import com.codebrig.omnisrc.structure.filter.RoleFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Assert
import org.junit.Test

class JavaNameTest extends OmniSRCTest {

    @Test
    void fileQualifiedName_noPackage() {
        def file = new File("src/test/resources/java/ForStmt.java")
        def resp = client.parse(file.name, file.text, SourceLanguage.Java.key(), Encoding.UTF8$.MODULE$)
        def fileFilter = new RoleFilter("FILE")

        fileFilter.getFilteredNodes(SourceLanguage.Java, resp.uast).each {
            Assert.assertEquals("ForStmt", it.qualifiedName)
        }
    }

    @Test
    void fileQualifiedName_withPackage() {
        def file = new File("src/test/resources/java/com.company/ForStmt_WithPackage.java")
        def resp = client.parse(file.name, file.text, SourceLanguage.Java.key(), Encoding.UTF8$.MODULE$)
        def fileFilter = new RoleFilter("FILE")

        fileFilter.getFilteredNodes(SourceLanguage.Java, resp.uast).each {
            Assert.assertEquals("com.company.ForStmt", it.qualifiedName)
        }
    }
}
