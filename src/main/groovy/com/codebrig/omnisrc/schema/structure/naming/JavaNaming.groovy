package com.codebrig.omnisrc.schema.structure.naming

import com.codebrig.omnisrc.SourceNode
import com.codebrig.omnisrc.schema.structure.StructureNaming
import com.codebrig.omnisrc.schema.filter.InternalRoleFilter
import com.codebrig.omnisrc.schema.filter.TypeFilter

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
            switch (it.internalType) {
                case "PrimitiveType":
                    type = it.token
                    break
                case "ParameterizedType":
                    type = getParameterizedTypeName(it)
                    break
                case "SimpleType":
                    type = getSimpleTypeName(it)
                    break
                default:
                    throw new IllegalStateException("Unsupported type: " + it.internalType)
            }
        }
        return type
    }

    private static String getParameterizedTypeName(SourceNode node) {
        def type = getSimpleTypeName(new InternalRoleFilter("type").getFilteredNodes(node.children).next())
        def paramTypes = ""
        new InternalRoleFilter("typeArguments").getFilteredNodes(node.children).each {
            paramTypes += getSimpleTypeName(it) + ","
        }
        if (paramTypes.endsWith(",")) {
            paramTypes = paramTypes.substring(0, paramTypes.length() - 1)
        }

        if (paramTypes.isEmpty()) {
            return type
        } else {
            return type + "<" + paramTypes + ">"
        }
    }

    private static String getSimpleTypeName(SourceNode node) {
        def name = getJavaQualifiedName(new InternalRoleFilter("name").getFilteredNodes(node.children).next().token)
        getImports(node.rootSourceNode).each {
            if (it.endsWith(name)) {
                name = it
            }
        }
        return name
    }

    private static List<String> getImports(SourceNode rootNode) {
        def importList = new ArrayList<String>()
        new InternalRoleFilter("imports").getFilteredNodes(rootNode.children).each {
            def importStr = ""
            new TypeFilter("SimpleName").getFilteredNodes(it).each {
                importStr += it.token + "."
            }
            if (importStr.endsWith(".")) {
                importStr = importStr.substring(0, importStr.length() - 1)
            }

            importList.add(importStr)
        }
        return importList
    }

    private static String getJavaQualifiedName(String object) {
        switch (object) {
            case "Boolean":
            case "Integer":
            case "String":
            case "Object":
                return "java.lang." + object
            default:
                return object
        }
    }
}
