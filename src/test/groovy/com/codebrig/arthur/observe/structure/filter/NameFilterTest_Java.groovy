package com.codebrig.arthur.observe.structure.filter

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.assertEquals

class NameFilterTest_Java extends ArthurTest {

    @Test
    void simpleName() {
        def file = new File("src/test/resources/java/ImportQualifiedName.java")
        def resp = client.parse(file.name, file.text, SourceLanguage.Java.key, Encoding.UTF8$.MODULE$)
        def fileFilter = new NameFilter("arg")

        fileFilter.getFilteredNodes(SourceLanguage.Java, resp.uast).each {
            assertEquals("SingleVariableDeclaration", it.internalType)
        }
    }

    @Test
    void innerVariable() {
        def file = new File("src/test/resources/java/ImportQualifiedName.java")
        def resp = client.parse(file.name, file.text, SourceLanguage.Java.key, Encoding.UTF8$.MODULE$)
        def fileFilter = new NameFilter("arrayList")

        fileFilter.getFilteredNodes(SourceLanguage.Java, resp.uast).each {
            assertEquals("VariableDeclarationStatement", it.internalType)
        }
    }
}