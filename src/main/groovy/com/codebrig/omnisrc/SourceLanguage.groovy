package com.codebrig.omnisrc

import com.codebrig.omnisrc.observe.structure.StructureLiteral
import com.codebrig.omnisrc.observe.structure.StructureNaming
import com.codebrig.omnisrc.observe.structure.literal.*
import com.codebrig.omnisrc.observe.structure.naming.*
import com.google.common.base.Charsets
import com.google.common.io.Files
import com.google.common.io.Resources

/**
 * The supported source code languages
 *
 * @version 0.3
 * @since 0.1
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
enum SourceLanguage {

    OmniSRC([]),
    Go(["go"]),
    Java(["java"]),
    Javascript(["js"]),
    Php(["php"]),
    Python(["py"]),
    Ruby(["rb"])

    private final Set<String> fileExtensions
    private StructureNaming namingCache
    private StructureLiteral literalCache

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

    boolean isValidExtension(String extension) {
        return fileExtensions.contains(extension.toLowerCase())
    }

    StructureNaming getStructureNaming() {
        if (namingCache != null) {
            return namingCache
        }
        switch (this) {
            case Go:
                return namingCache = new GoNaming()
            case Java:
                return namingCache = new JavaNaming()
            case Javascript:
                return namingCache = new JavascriptNaming()
            case Php:
                return namingCache = new PhpNaming()
            case Python:
                return namingCache = new PythonNaming()
            case Ruby:
                return namingCache = new RubyNaming()
            default:
                return null //todo: implement rest
        }
    }

    StructureLiteral getStructureLiteral() {
        if (literalCache != null) {
            return literalCache
        }
        switch (this) {
            case Go:
                return literalCache = new GoLiteral()
            case Java:
                return literalCache = new JavaLiteral()
            case Javascript:
                return literalCache = new JavascriptLiteral()
            case Php:
                return literalCache = new PhpLiteral()
            case Python:
                return literalCache = new PythonLiteral()
            case Ruby:
                return literalCache = new RubyLiteral()
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

    static List<SourceLanguage> getSupportedLanguages() {
        return values().findAll { it != OmniSRC }
    }
}
