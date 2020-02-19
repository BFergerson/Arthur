package com.codebrig.arthur.observe.structure.filter

import com.codebrig.arthur.ArthurTest
import com.codebrig.arthur.SourceLanguage
import gopkg.in.bblfsh.sdk.v1.protocol.generated.Encoding
import org.junit.Test

import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue

class CompilationUnitTest extends ArthurTest {

    @Test
    void onlyCompilationUnits_Java() {
        assertCompilationUnitPresent(new File("src/test/resources/same/functions/Functions.java"))
    }

    @Test
    void onlyCompilationUnits_Javascript() {
        assertCompilationUnitPresent(new File("src/test/resources/same/functions/Functions.js"))
    }

    @Test
    void onlyCompilationUnits_Go() {
        assertCompilationUnitPresent(new File("src/test/resources/same/functions/Functions.go"))
    }

    @Test
    void onlyCompilationUnits_Php() {
        assertCompilationUnitPresent(new File("src/test/resources/same/functions/Functions.php"))
    }

    @Test
    void onlyCompilationUnits_Python() {
        assertCompilationUnitPresent(new File("src/test/resources/same/functions/Functions.py"))
    }

    @Test
    void onlyCompilationUnits_Ruby() {
        assertCompilationUnitPresent(new File("src/test/resources/same/functions/Functions.rb"))
    }

    @Test
    void onlyCompilationUnits_CSharp() {
        assertCompilationUnitPresent(new File("src/test/resources/same/functions/Functions.cs"))
    }

    @Test
    void onlyCompilationUnits_CPlusPlus() {
        assertCompilationUnitPresent(new File("src/test/resources/same/functions/Functions.cpp"))
    }

    @Test
    void onlyCompilationUnits_Bash() {
        assertCompilationUnitPresent(new File("src/test/resources/same/functions/Functions.sh"))
    }

    private static void assertCompilationUnitPresent(File file) {
        def language = SourceLanguage.getSourceLanguage(file)
        def resp = client.parse(file.name, file.text, language.babelfishName, Encoding.UTF8$.MODULE$)

        boolean foundCompilationUnit = false
        new CompilationUnitFilter().getFilteredNodes(language, resp.uast).each {
            assertFalse(foundCompilationUnit)
            foundCompilationUnit = true
        }
        assertTrue(foundCompilationUnit)
    }
}