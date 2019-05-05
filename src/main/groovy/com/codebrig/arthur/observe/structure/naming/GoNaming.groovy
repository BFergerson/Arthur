package com.codebrig.arthur.observe.structure.naming

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureNaming
import com.codebrig.arthur.observe.structure.filter.InternalRoleFilter

/**
 * Used to get the names of Go AST nodes
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class GoNaming implements StructureNaming {

    @Override
    boolean isNamedNodeType(String internalType) {
        switch (Objects.requireNonNull(internalType)) {
            case "FuncDecl":
                return true
            case "Ident":
                return true
            default:
                return false
        }
    }

    @Override
    String getNodeName(SourceNode node) {
        switch (Objects.requireNonNull(node).internalType) {
            case "FuncDecl":
                return getFuncDeclName(node)
            case "Ident":
                return getIdentName(node)
            default:
                throw new IllegalArgumentException("Unsupported Go node type: " + node.internalType)
        }
    }

    static String getFuncDeclName(SourceNode node) {
        def functionName = ""
        new InternalRoleFilter("Name").getFilteredNodes(node.children).each {
            functionName = it.token
        }
        return functionName + "()"
    }

    static String getIdentName(SourceNode node) {
        return node.token
    }
}
