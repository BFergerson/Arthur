package com.codebrig.omnisrc.structure.naming

import com.codebrig.omnisrc.SourceNode
import com.codebrig.omnisrc.structure.StructureNaming
import com.codebrig.omnisrc.structure.filter.InternalRoleFilter
import com.codebrig.omnisrc.structure.filter.TypeFilter

/**
 * todo: description
 *
 * @version 0.2
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class JavaNaming implements StructureNaming {

    @Override
    String getNodeName(SourceNode node) {
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

        name += "("
        new InternalRoleFilter("parameters").getFilteredNodes(node.children).each {
            name += getSingleVariableDeclarationName(it) + ","
        }
        if (name.endsWith(",")) {
            name = name.substring(0, name.length() - 1)
        }
        return name + ")"
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

    private static String getSingleVariableDeclarationName(SourceNode node) {
        def type = ""
        new InternalRoleFilter("type").getFilteredNodes(node.children).each {
            if (it.internalType == "PrimitiveType") {
                type = it.token
            } else {
                type = getJavaQualifiedName(new InternalRoleFilter("name").getFilteredNodes(it.children).next().token)
            }
        }
        return type
    }


    private static String getJavaQualifiedName(String object) {
        switch (object) {
            case "String":
            case "Object":
                return "java.lang." + object
            default:
                return object
        }
    }
}
