package com.codebrig.arthur

import com.codebrig.arthur.observe.structure.StructureLiteral
import com.codebrig.arthur.observe.structure.StructureNaming
import com.codebrig.arthur.observe.structure.literal.*
import com.codebrig.arthur.observe.structure.naming.*
import com.google.common.base.Charsets
import com.google.common.io.Files
import com.google.common.io.Resources

/**
 * The supported source code languages
 *
 * @version 0.3.2
 * @since 0.1
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
enum SourceLanguage {

    Omnilingual([]),
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
        if (this == Omnilingual) {
            return Omnilingual.name()
        }
        return key.substring(0, 1).toUpperCase() + key.substring(1)
    }

    String getBaseStructureSchemaDefinition() {
        if (this == Omnilingual) {
            return Resources.toString(Resources.getResource(
                    "schema/omnilingual/Arthur_Omnilingual_Base_Structure.gql"), Charsets.UTF_8)
        } else {
            return Resources.toString(Resources.getResource(
                    "schema/unilingual/$key/Arthur_" + qualifiedName + "_Base_Structure.gql"), Charsets.UTF_8)
        }
    }

    String getSemanticRolesSchemaDefinition() {
        if (this == Omnilingual) {
            return Resources.toString(Resources.getResource(
                    "schema/omnilingual/Arthur_Omnilingual_Semantic_Roles.gql"), Charsets.UTF_8)
        } else {
            return Resources.toString(Resources.getResource(
                    "schema/unilingual/$key/Arthur_" + qualifiedName + "_Semantic_Roles.gql"), Charsets.UTF_8)
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
                throw new IllegalStateException("Missing structure naming for language: " + this)
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
                throw new IllegalStateException("Missing structure literal for language: " + this)
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
        return values().findAll { it != Omnilingual }
    }
}
