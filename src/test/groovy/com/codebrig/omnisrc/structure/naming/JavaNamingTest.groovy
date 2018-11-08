package com.codebrig.omnisrc.structure.naming

import com.codebrig.omnisrc.OmniSRCTest
import com.codebrig.omnisrc.SourceLanguage
import com.codebrig.omnisrc.structure.filter.RoleFilter
import com.codebrig.omnisrc.structure.filter.TypeFilter
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Assert
import org.junit.Test

import static org.junit.Assert.assertTrue

class JavaNamingTest extends OmniSRCTest {

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
        def file = new File("src/test/resources/java/com/company/ForStmt_WithPackage.java")
        def resp = client.parse(file.name, file.text, SourceLanguage.Java.key(), Encoding.UTF8$.MODULE$)
        def fileFilter = new RoleFilter("FILE")

        fileFilter.getFilteredNodes(SourceLanguage.Java, resp.uast).each {
            Assert.assertEquals("com.company.ForStmt", it.qualifiedName)
        }
    }

    @Test
    void methodQualifiedName_noPackage() {
        def file = new File("src/test/resources/java/ForStmt.java")
        def resp = client.parse(file.name, file.text, SourceLanguage.Java.key(), Encoding.UTF8$.MODULE$)
        def functionFilter = new TypeFilter("MethodDeclaration")

        functionFilter.getFilteredNodes(SourceLanguage.Java, resp.uast).each {
            Assert.assertEquals("ForStmt.method()", it.qualifiedName)
        }
    }

    @Test
    void methodQualifiedName_withPackage() {
        def file = new File("src/test/resources/java/com/company/ForStmt_WithPackage.java")
        def resp = client.parse(file.name, file.text, SourceLanguage.Java.key(), Encoding.UTF8$.MODULE$)
        def functionFilter = new TypeFilter("MethodDeclaration")

        functionFilter.getFilteredNodes(SourceLanguage.Java, resp.uast).each {
            Assert.assertEquals("com.company.ForStmt.method()", it.qualifiedName)
        }
    }

    @Test
    void methodQualifiedName_variousStuff() {
        def file = new File("src/test/resources/java/VariousStuff.java")
        def resp = client.parse(file.name, file.text, SourceLanguage.Java.key(), Encoding.UTF8$.MODULE$)
        def functionFilter = new TypeFilter("MethodDeclaration")

        boolean foundMethod3 = false
        boolean foundMethod4 = false
        boolean foundMethod5 = false
        functionFilter.getFilteredNodes(SourceLanguage.Java, resp.uast).each {
            switch (it.qualifiedName) {
                case "VariousStuff.method_3_args(java.lang.String,int,java.lang.Object)":
                    foundMethod3 = true
                    break
                case "VariousStuff.method_4_args(byte,java.lang.String,int,java.lang.Object)":
                    foundMethod4 = true
                    break
                case "VariousStuff.method_5_args(byte,java.lang.String,int,java.lang.Object,char)":
                    foundMethod5 = true
                    break
                default:
                    throw new IllegalArgumentException("Invalid qualified name: " + it.qualifiedName)
            }
        }
        assertTrue(foundMethod3)
        assertTrue(foundMethod4)
        assertTrue(foundMethod5)
    }
}
