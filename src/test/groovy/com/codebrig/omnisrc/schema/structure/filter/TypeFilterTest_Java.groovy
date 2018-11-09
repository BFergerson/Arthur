package com.codebrig.omnisrc.schema.structure.filter

import com.codebrig.omnisrc.OmniSRCTest
import com.codebrig.omnisrc.SourceLanguage
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

class TypeFilterTest_Java extends OmniSRCTest {

    @Test
    void onlyMethodsFilter() {
        def methodFilter = new TypeFilter("MethodDeclaration")
        def file = new File("src/test/resources/java/Complex.java")
        def resp = client.parse(file.name, file.text, SourceLanguage.Java.key(), Encoding.UTF8$.MODULE$)

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
        def resp = client.parse(file.name, file.text, SourceLanguage.getSourceLanguage(file).key(), Encoding.UTF8$.MODULE$)

        boolean foundCompilationUnit = false
        boolean foundMethod = false
        filter.getFilteredNodes(SourceLanguage.Java, resp.uast).each {
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