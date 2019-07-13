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
        def methodFilter = new TypeFilter("MethodDeclaration")
        def file = new File("src/test/resources/java/Complex.java")
        def resp = client.parse(file.name, file.text, SourceLanguage.Java.key)

        boolean foundMethod = false
        methodFilter.getFilteredNodes(SourceLanguage.Java, resp.uast).each {
            assertEquals("MethodDeclaration", it.internalType)
            foundMethod = true
        }
        assertTrue(foundMethod)
    }

    @Test
    void compilationUnitsAndMethodsFilter() {
        def filter = new TypeFilter("CompilationUnit", "MethodDeclaration")
        def file = new File("src/test/resources/java/Complex.java")
        def resp = client.parse(file.name, file.text, SourceLanguage.getSourceLanguage(file).key)
        def rootNode = new BblfshClient.UastMethods(resp.uast()).decode().root().load()

        boolean foundCompilationUnit = false
        boolean foundMethod = false
        filter.getFilteredNodes(SourceLanguage.Java, rootNode).each {
            assertTrue(["CompilationUnit", "MethodDeclaration"].contains(it.internalType))
            switch (it.internalType) {
                case "CompilationUnit":
                    foundCompilationUnit = true
                    break
                case "MethodDeclaration":
                    foundMethod = true
            }
        }
        assertTrue(foundCompilationUnit)
        assertTrue(foundMethod)
    }

}