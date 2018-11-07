package com.codebrig.omnisrc.structure.name

import com.codebrig.omnisrc.SourceNode
import com.codebrig.omnisrc.structure.filter.TypeFilter

class JavaName {

    static String getNodeName(SourceNode node) {
        switch (Objects.requireNonNull(node).internalType) {
            case "CompilationUnit":
                return getCompilationUnitName(node)
            case "MethodDeclaration":
                def packageName = getCompilationUnitName(node.rootSourceNode)
                if (packageName.isEmpty()) {
                    return getMethodDeclarationName(node)
                } else {
                    return packageName + "." + getMethodDeclarationName(node)
                }
            case "TypeDeclaration":
                return getTypeDeclarationName(node)
            default:
                throw new IllegalArgumentException("Unsupported Java node type: " + node.internalType)
        }
    }

    static String getCompilationUnitName(SourceNode node) {
        def name = ""
        node.children.each {
            if (it.internalType == "PackageDeclaration") {
                name += getPackageDeclarationName(it)
            } else if (it.internalType == "TypeDeclaration") {
                name += getTypeDeclarationName(it)
            }
        }
        return name
    }

    static String getMethodDeclarationName(SourceNode node) {
        def name = ""
        new TypeFilter("SimpleName").getFilteredNodes(node.children).each {
            name += it.token
        }
        return name
    }

    static String getTypeDeclarationName(SourceNode node) {
        def name = ""
        new TypeFilter("SimpleName").getFilteredNodes(node.children).each {
            name += it.token
        }
        return name
    }

    private static String getPackageDeclarationName(SourceNode node) {
        def name = ""
        new TypeFilter("SimpleName").getFilteredNodes(node).each {
            name += it.token + "."
        }
        return name
    }
}
