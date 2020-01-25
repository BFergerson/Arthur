package com.codebrig.arthur.observe.structure.naming

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureNaming
import com.codebrig.arthur.observe.structure.filter.InternalRoleFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.RoleFilter
import com.codebrig.arthur.observe.structure.filter.TypeFilter

import static com.codebrig.arthur.observe.structure.naming.util.NamingUtils.trimTrailingComma

/**
 * Used to get the names/qualified names of Java AST nodes
 *
 * @version 0.4
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 * @author <a href="mailto:valpecaoco@gmail.com">Val Pecaoco</a>
 */
class JavaNaming extends StructureNaming {

    @Override
    boolean isNamedNodeType(String internalType) {
        switch (Objects.requireNonNull(internalType)) {
            case "CompilationUnit":
            case "FieldDeclaration":
            case "MethodDeclaration":
            case "SimpleName":
            case "SingleVariableDeclaration":
            case "TypeDeclaration":
            case "VariableDeclaration":
            case "QualifiedName":
                return true
            default:
                return false
        }
    }

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
            case "FieldDeclaration":
                return getFieldDeclarationName(node)
            case "SingleVariableDeclaration":
                return getSingleVariableDeclarationName(node)
            case "SimpleName":
                return getSimpleNameName(node)
            case "QualifiedName":
                return getQualifiedNameName(node)
            default:
                return super.getNodeName(node)
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
            name += getSingleVariableDeclarationTypeName(it) + ","
        }
        name = trimTrailingComma(name)
        return name + ")"
    }

    static String getTypeDeclarationName(SourceNode node) {
        def name = ""
        new TypeFilter("SimpleName").getFilteredNodes(node.children).each {
            name += it.token
        }
        return name
    }

    static String getSingleVariableDeclarationName(SourceNode node) {
        def name = ""
        new TypeFilter("SimpleName").getFilteredNodes(node.children).each {
            name += it.token
        }
        return name
    }

    static String getFieldDeclarationName(SourceNode node) {
        String fieldDeclarationName = ""
        new TypeFilter("VariableDeclarationFragment").getFilteredNodes(node.children).each {
            fieldDeclarationName = MultiFilter.matchAll(
                    new RoleFilter("EXPRESSION"), new RoleFilter("IDENTIFIER"))
                    .getFilteredNodes(it.children).next().token
        }
        return fieldDeclarationName
    }

    static String getPackageDeclarationName(SourceNode node) {
        def name = ""
        new TypeFilter("SimpleName").getFilteredNodes(node).each {
            name += it.token + "."
        }
        return name
    }

    static String getSimpleNameName(SourceNode node) {
        def name = ""
        new TypeFilter("SimpleName").getFilteredNodes(node).each {
            name += it.token
        }
        return name
    }

    static String getQualifiedNameName(SourceNode node) {
        def name = ""
        new TypeFilter("SimpleName").getFilteredNodes(node).each {
            name += it.token + "."
        }
        if (name.endsWith(".")) {
            name = name.substring(0, name.length() - 1)
        }
        return name
    }

    static String getSingleVariableDeclarationTypeName(SourceNode node) {
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
                case "ArrayType":
                    type = getArrayTypeName(it)
                    break
                case "QualifiedType":
                    type = getQualifiedTypeName(it)
                    break
                default:
                    throw new IllegalStateException("Unsupported variable declaration node type: " + it.internalType)
            }
        }
        return type
    }

    static String getParameterizedTypeName(SourceNode node) {
        def type = getSimpleTypeName(new InternalRoleFilter("type").getFilteredNodes(node.children).next())
        def paramTypes = ""
        new InternalRoleFilter("typeArguments").getFilteredNodes(node.children).each {
            if (it.internalType == "WildcardType") {
                paramTypes += "?,"
            } else if (it.internalType == "ParameterizedType") {
                paramTypes += getParameterizedTypeName(it) + ","
            } else {
                paramTypes += getSimpleTypeName(it) + ","
            }
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

    static String getSimpleTypeName(SourceNode node) {
        def name = ""
        new TypeFilter("SimpleName").getFilteredNodes(node).each {
            name += it.token + "."
        }
        if (name.endsWith(".")) {
            name = name.substring(0, name.length() - 1)
        }
        name = getJavaQualifiedName(name)

        getImports(node.rootSourceNode).each {
            if (it.endsWith(name)) {
                name = it
            }
        }
        return name
    }

    static String getArrayTypeName(SourceNode node) {
        def type
        def elementType = new InternalRoleFilter("elementType").getFilteredNodes(node.children).next()
        switch (elementType.internalType) {
            case "PrimitiveType":
                type = elementType.token
                break
            case "ParameterizedType":
                type = getParameterizedTypeName(elementType)
                break
            case "SimpleType":
                type = getSimpleTypeName(elementType)
                break
            case "ArrayType":
                type = getArrayTypeName(elementType)
                break
            default:
                throw new IllegalStateException("Unsupported array argument node type: " + elementType.internalType)
        }

        def dimensions = ""
        new InternalRoleFilter("dimensions").getFilteredNodes(node.children).each {
            dimensions += "[]"
        }
        return type + dimensions
    }

    static String getQualifiedTypeName(SourceNode node) {
        def qualifier = ""
        new InternalRoleFilter("qualifier").getFilteredNodes(node.children).each {
            switch (it.internalType) {
                case "PrimitiveType":
                    qualifier += it.token
                    break
                case "ParameterizedType":
                    qualifier += getParameterizedTypeName(it)
                    break
                case "SimpleType":
                    qualifier += getSimpleTypeName(it)
                    break
                case "ArrayType":
                    qualifier += getArrayTypeName(it)
                    break
                case "QualifiedType":
                    qualifier += getQualifiedTypeName(it)
                    break
                default:
                    throw new IllegalStateException("Unsupported qualified argument node type: " + it.internalType)
            }
        }

        def name = getSimpleTypeName(new InternalRoleFilter("name").getFilteredNodes(node.children).next())
        return qualifier + "." + name
    }

    static List<String> getImports(SourceNode rootNode) {
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

    static String getJavaQualifiedName(String object) {
        switch (object) {
            case "Boolean":
            case "Double":
            case "Integer":
            case "String":
            case "Object":
            case "Iterable":
                return "java.lang." + object
            default:
                return object
        }
    }
}
