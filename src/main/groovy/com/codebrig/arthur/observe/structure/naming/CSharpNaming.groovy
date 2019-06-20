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
class CSharpNaming implements StructureNaming {

    @Override
    boolean isNamedNodeType(String internalType) {
        switch (Objects.requireNonNull(internalType)) {
            case "MethodDeclaration":
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
            default:
                throw new IllegalArgumentException("Unsupported C# node type: " + node.internalType)
        }
    }

    static String getMethodDeclarationName(SourceNode node) {
        def name = ""
        new TypeFilter("IdentifierToken").getFilteredNodes(node.children).each {
            name += it.token
        }

        name += "("
        new TypeFilter("ParameterList").getFilteredNodes(node.children).each {
            new TypeFilter("Parameter").getFilteredNodes(it.children).each {
                name += getSingleVariableDeclarationName(it) + ","
            }
        }
        name = trimTrailingComma(name)
        return name + ")"
    }

    static String getSingleVariableDeclarationName(SourceNode node) {
        def type = ""
        new TypeFilter("PredefinedType").getFilteredNodes(node.children).each {
            it.children.each {
                switch (it.internalType) {
                    case "IntKeyword":
                    case "StringKeyword":
                        type = it.token
                        break
                    default:
                        throw new IllegalStateException("Unsupported variable declaration node type: " + it.internalType)
                }
            }
        }
        return type
    }
}
