package com.codebrig.omnisrc

import com.codebrig.omnisrc.schema.structure.StructureNaming
import com.codebrig.omnisrc.schema.structure.naming.JavaNaming
import com.google.common.base.Charsets
import com.google.common.io.Files
import com.google.common.io.Resources

/**
 * todo: description
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

    String key() {
        return name().toLowerCase()
    }

    String getQualifiedName() {
        if (this == OmniSRC) {
            return "Omnilingual"
        }
        return key().substring(0, 1).toUpperCase() + key().substring(1)
    }

    String getSchemaDefinitionName() {
        return "OmniSRC_" + qualifiedName + "_Schema"
    }

    String getFullSchemaDefinition(String version) {
        if (this == OmniSRC) {
            return Resources.toString(Resources.getResource(
                    "schema/omnilingual/$schemaDefinitionName-$version" + ".gql"), Charsets.UTF_8)
        } else {
            return Resources.toString(Resources.getResource(
                    "schema/unilingual/" + key() + "/$schemaDefinitionName-$version" + ".gql"), Charsets.UTF_8)
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
