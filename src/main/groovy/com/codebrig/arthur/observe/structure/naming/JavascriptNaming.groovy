package com.codebrig.arthur.observe.structure.naming

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureNaming
import com.codebrig.arthur.observe.structure.filter.InternalRoleFilter

/**
 * Used to get the names of JavaScript AST nodes
 *
 * @version 0.3.2
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class JavascriptNaming implements StructureNaming {

    @Override
    boolean isNamedNodeType(String internalType) {
        switch (Objects.requireNonNull(internalType)) {
            case "FunctionDeclaration":
                return true
            default:
                return false
        }
    }

    @Override
    String getNodeName(SourceNode node) {
        switch (Objects.requireNonNull(node).internalType) {
            case "FunctionDeclaration":
                return getFunctionDeclarationName(node)
            default:
                return null
        }
    }

    static String getFunctionDeclarationName(SourceNode node) {
        def functionName = ""
        new InternalRoleFilter("id").getFilteredNodes(node.children).each {
            functionName = it.token
        }
        return functionName + "()"
    }
}
