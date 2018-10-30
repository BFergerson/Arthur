package com.codebrig.omnisrc

/**
 * @author github.com/BFergerson
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

    boolean isValidExtension(String extension) {
        return fileExtensions.contains(extension.toLowerCase())
    }
}
