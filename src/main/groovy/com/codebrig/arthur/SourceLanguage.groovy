package com.codebrig.arthur

import com.codebrig.arthur.observe.structure.StructureLiteral
import com.codebrig.arthur.observe.structure.StructureNaming
import com.codebrig.arthur.observe.structure.literal.*
import com.codebrig.arthur.observe.structure.naming.*
import com.google.common.base.Charsets
import com.google.common.io.Files
import com.google.common.io.Resources
import groovy.transform.Memoized

/**
 * The supported source code languages
 *
 * @version 0.4
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

    @Memoized
    StructureNaming getStructureNaming() {
        switch (this) {
            case Go:
                return new GoNaming()
            case Java:
                return new JavaNaming()
            case Javascript:
                return new JavascriptNaming()
            case Php:
                return new PhpNaming()
            case Python:
                return new PythonNaming()
            case Ruby:
                return new RubyNaming()
            default:
                throw new IllegalStateException("Missing structure naming for language: " + this)
        }
    }

    @Memoized
    StructureLiteral getStructureLiteral() {
        switch (this) {
            case Go:
                return new GoLiteral()
            case Java:
                return new JavaLiteral()
            case Javascript:
                return new JavascriptLiteral()
            case Php:
                return new PhpLiteral()
            case Python:
                return new PythonLiteral()
            case Ruby:
                return new RubyLiteral()
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
