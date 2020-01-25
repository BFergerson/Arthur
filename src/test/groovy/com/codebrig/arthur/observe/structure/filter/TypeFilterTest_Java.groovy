package com.codebrig.arthur.observe.structure.filter

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import org.bblfsh.client.v2.BblfshClient
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

class TypeFilterTest_Java extends ArthurTest {

    @Test
    void onlyMethodsFilter() {
        def methodFilter = new FunctionFilter()
        def file = new File("src/test/resources/java/Complex.java")
        def resp = client.parse(file.name, file.text, SourceLanguage.Java.babelfishName)
        def rootNode = new BblfshClient.UastMethods(resp.uast()).decode().root().load()

        boolean foundMethod = false
        methodFilter.getFilteredNodes(SourceLanguage.Java, rootNode).each {
            assertEquals("uast:FunctionGroup", it.internalType)
            foundMethod = true
        }
        assertTrue(foundMethod)
    }

    @Test
    void compilationUnitsAndMethodsFilter() {
        def filter = new TypeFilter("CompilationUnit", "uast:FunctionGroup")
        def file = new File("src/test/resources/java/Complex.java")
        def resp = client.parse(file.name, file.text, SourceLanguage.getSourceLanguage(file).babelfishName)
        def rootNode = new BblfshClient.UastMethods(resp.uast()).decode().root().load()

        boolean foundCompilationUnit = false
        boolean foundMethod = false
        filter.getFilteredNodes(SourceLanguage.Java, rootNode).each {
            assertTrue(["CompilationUnit", "uast:FunctionGroup"].contains(it.internalType))
            switch (it.internalType) {
                case "CompilationUnit":
                    foundCompilationUnit = true
                    break
                case "uast:FunctionGroup":
                    foundMethod = true
            }
        }
        assertTrue(foundCompilationUnit)
        assertTrue(foundMethod)
    }
}