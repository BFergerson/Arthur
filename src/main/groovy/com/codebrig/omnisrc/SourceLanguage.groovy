package com.codebrig.omnisrc

import com.codebrig.omnisrc.observe.structure.StructureLiteral
import com.codebrig.omnisrc.observe.structure.StructureNaming
import com.codebrig.omnisrc.observe.structure.literal.GoLiteral
import com.codebrig.omnisrc.observe.structure.literal.JavaLiteral
import com.codebrig.omnisrc.observe.structure.literal.JavascriptLiteral
import com.codebrig.omnisrc.observe.structure.literal.PythonLiteral
import com.codebrig.omnisrc.observe.structure.naming.JavaNaming
import com.google.common.base.Charsets
import com.google.common.io.Files
import com.google.common.io.Resources

/**
 * The supported source code languages
 *
 * @version 0.2
 * @since 0.1
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
enum SourceLanguage {

    OmniSRC([]),
    //Bash(["csh", "tcsh", "bash", "sh", "zsh"]),
    Go(["go"]),
    Java(["java"]),
    Javascript(["js"]),
    Php(["php"]),
    Python(["py"]),
    Ruby(["rb"])

    private final Set<String> fileExtensions

    SourceLanguage(List<String> fileExtensions) {
        this.fileExtensions = new HashSet<>(fileExtensions)
    }

    String getKey() {
        return name().toLowerCase()
    }

    String getQualifiedName() {
        if (this == OmniSRC) {
            return "Omnilingual"
        }
        return key.substring(0, 1).toUpperCase() + key.substring(1)
    }

    String getBaseStructureSchemaDefinition() {
        if (this == OmniSRC) {
            return Resources.toString(Resources.getResource(
                    "schema/omnilingual/OmniSRC_Omnilingual_Base_Structure.gql"), Charsets.UTF_8)
        } else {
            return Resources.toString(Resources.getResource(
                    "schema/unilingual/$key/OmniSRC_" + qualifiedName + "_Base_Structure.gql"), Charsets.UTF_8)
        }
    }

    String getIndividualSemanticRolesSchemaDefinition() {
        if (this == OmniSRC) {
            return Resources.toString(Resources.getResource(
                    "schema/omnilingual/OmniSRC_Omnilingual_Individual_Semantic_Roles.gql"), Charsets.UTF_8)
        } else {
            return Resources.toString(Resources.getResource(
                    "schema/unilingual/$key/OmniSRC_" + qualifiedName + "_Individual_Semantic_Roles.gql"), Charsets.UTF_8)
        }
    }

    String getActualSemanticRolesSchemaDefinition() {
        if (this == OmniSRC) {
            return Resources.toString(Resources.getResource(
                    "schema/omnilingual/OmniSRC_Omnilingual_Actual_Semantic_Roles.gql"), Charsets.UTF_8)
        } else {
            return Resources.toString(Resources.getResource(
                    "schema/unilingual/$key/OmniSRC_" + qualifiedName + "_Actual_Semantic_Roles.gql"), Charsets.UTF_8)
        }
    }

    String getPossibleSemanticRolesSchemaDefinition() {
        if (this == OmniSRC) {
            return Resources.toString(Resources.getResource(
                    "schema/omnilingual/OmniSRC_Omnilingual_Possible_Semantic_Roles.gql"), Charsets.UTF_8)
        } else {
            return Resources.toString(Resources.getResource(
                    "schema/unilingual/$key/OmniSRC_" + qualifiedName + "_Possible_Semantic_Roles.gql"), Charsets.UTF_8)
        }
    }

    boolean isValidExtension(String extension) {
        return fileExtensions.contains(extension.toLowerCase())
    }

    StructureNaming getStructureNaming() {
        switch (this) {
            case Java:
                return new JavaNaming()
            default:
                return null //todo: implement rest
        }
    }

    StructureLiteral getStructureLiteral() {
        switch (this) {
            case Go:
                return new GoLiteral()
            case Java:
                return new JavaLiteral()
            case Javascript:
                return new JavascriptLiteral()
            case Python:
                return new PythonLiteral()
            default:
                return null //todo: implement rest
        }
    }

    static boolean isSourceLanguageKnown(File file) {
        def fileExtension = Files.getFileExtension(file.name)
        return values().any { it.isValidExtension(fileExtension) }
    }

    static SourceLanguage getSourceLanguage(File file) {
        def sourceLanguage
        def fileExtension = Files.getFileExtension(file.name)
        values().each {
            if (it.isValidExtension(fileExtension)) {
                sourceLanguage = it
            }
        }
        if (sourceLanguage != null) {
            return sourceLanguage
        } else {
            throw new IllegalArgumentException("Could not detect source code language of file: " + file)
        }
    }

    static SourceLanguage getSourceLanguageByName(String languageName) {
        def sourceLanguage
        values().each {
            if (it.name().toLowerCase() == languageName.toLowerCase()) {
                sourceLanguage = it
            }
        }
        if (sourceLanguage != null) {
            return sourceLanguage
        } else {
            throw new IllegalArgumentException("Could not determine source language of: " + languageName)
        }
    }
}
