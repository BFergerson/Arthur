package com.codebrig.arthur.observe.structure.filter

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import org.bblfsh.client.v2.BblfshClient
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

class NameFilterTest_Java extends ArthurTest {

    @Test
    void simpleName() {
        def file = new File("src/test/resources/java/ImportQualifiedName.java")
        def resp = client.parse(file.name, file.text, SourceLanguage.Java.babelfishName)
        def rootNode = new BblfshClient.UastMethods(resp.uast()).decode().root().load()
        def nameFilter = new NameFilter("arg")

        nameFilter.getFilteredNodes(SourceLanguage.Java, rootNode).each {
            assertTrue(["uast:Identifier", "uast:Argument"].stream().anyMatch(
                    { t -> t == it.internalType })
            )
        }
    }

    @Test
    void innerVariable() {
        def file = new File("src/test/resources/java/ImportQualifiedName.java")
        def resp = client.parse(file.name, file.text, SourceLanguage.Java.babelfishName)
        def rootNode = new BblfshClient.UastMethods(resp.uast()).decode().root().load()
        def nameFilter = new NameFilter("arrayList")

        nameFilter.getFilteredNodes(SourceLanguage.Java, rootNode).each {
            assertEquals("uast:Identifier", it.internalType)
        }
    }
}