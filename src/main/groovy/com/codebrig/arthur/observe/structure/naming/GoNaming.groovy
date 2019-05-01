package com.codebrig.arthur.observe.structure.naming

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureNaming
import com.codebrig.arthur.observe.structure.filter.InternalRoleFilter

/**
 * Used to get the names of Go AST nodes
 *
 * @version 0.3.2
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class GoNaming implements StructureNaming {

    @Override
    boolean isNamedNodeType(String internalType) {
        switch (Objects.requireNonNull(internalType)) {
            case "FuncDecl":
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
}
