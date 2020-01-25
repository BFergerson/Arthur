package com.codebrig.arthur.observe.structure.naming

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureNaming
import com.codebrig.arthur.observe.structure.filter.TypeFilter

import static com.codebrig.arthur.observe.structure.naming.util.NamingUtils.trimTrailingComma

/**
 * Used to get the names/qualified names of C# AST nodes
 *
 * @version 0.4
 * @since 0.4
 * @author <a href="mailto:valpecaoco@gmail.com">Val Pecaoco</a>
 */
class CSharpNaming extends StructureNaming {

    @Override
    boolean isNamedNodeType(String internalType) {
        switch (Objects.requireNonNull(internalType)) {
            case "MethodDeclaration":
            case "VariableDeclaration":
                return true
            default:
                return false
        }
    }

    @Override
    String getNodeName(SourceNode node) {
        switch (Objects.requireNonNull(node).internalType) {
            case "MethodDeclaration":
                return getMethodDeclarationName(node)
            case "VariableDeclaration":
                return getVariableDeclarationName(node)
            default:
                return super.getNodeName(node)
        }
    }

    static String getMethodDeclarationName(SourceNode node) {
        def name = getIdentifierToken(node.children)
        name += "("
        new TypeFilter("ParameterList").getFilteredNodes(node.children).each {
            new TypeFilter("Parameter").getFilteredNodes(it.children).each {
                name += getParameterName(it) + ","
            }
        }
        name = trimTrailingComma(name)
        return name + ")"
    }

    static String getVariableDeclarationName(SourceNode node) {
        def name = ""
        new TypeFilter("VariableDeclarator").getFilteredNodes(node.children).each {
            name = getIdentifierToken(it)
        }
        return name
    }

    static String getParameterName(SourceNode node) {
        def name = ""
        def matched = new TypeFilter("ArrayType").getFilteredNodes(node.children)
        if (matched.hasNext()) {
            name = getPredefinedTypeName(matched.next())
        }
        matched = new TypeFilter("GenericName").getFilteredNodes(node.children)
        if (matched.hasNext()) {
            name = getGenericNameName(matched.next())
        }
        new TypeFilter("IdentifierName").getFilteredNodes(node.children).each {
            name = getIdentifierToken(it.children)
        }
        if (name.isEmpty()) {
            name = getPredefinedTypeName(node)
        }
        return name
    }

    static String getPredefinedTypeName(SourceNode node) {
        def name = ""
        new TypeFilter("PredefinedType").getFilteredNodes(node.children).each {
            it.children.each {
                name = it.token
            }
        }
        return name
    }

    static String getGenericNameName(SourceNode node) {
        def name = getIdentifierToken(node.children)
        new TypeFilter("TypeArgumentList").getFilteredNodes(node.children).each {
            name += "<"
            name += getIdentifierToken(it)
            name += ">"
        }
        return name
    }

    static String getIdentifierToken(Iterator<SourceNode> node) {
        def name = ""
        new TypeFilter("IdentifierToken").getFilteredNodes(node).each {
            name = it.token
        }
        return name
    }

    static String getIdentifierToken(SourceNode node) {
        def name = ""
        new TypeFilter("IdentifierToken").getFilteredNodes(node).each {
            name = it.token
        }
        return name
    }
}
