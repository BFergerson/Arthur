package com.codebrig.omnisrc.observe.structure.naming

import com.codebrig.omnisrc.SourceNode
import com.codebrig.omnisrc.observe.structure.StructureNaming
import com.codebrig.omnisrc.observe.structure.filter.InternalRoleFilter

/**
 * Used to get the names of PHP AST nodes
 *
 * @version 0.3.2
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class PhpNaming implements StructureNaming {

    @Override
    boolean isNamedNodeType(String internalType) {
        switch (Objects.requireNonNull(internalType)) {
            case "Stmt_Function":
                return true
            default:
                return false
        }
    }

    @Override
    String getNodeName(SourceNode node) {
        switch (Objects.requireNonNull(node).internalType) {
            case "Stmt_Function":
                return getFunctionName(node)
            default:
                return null
        }
    }

    static String getFunctionName(SourceNode node) {
        def functionName = ""
        new InternalRoleFilter("Name").getFilteredNodes(node.children).each {
            functionName = it.token
        }
        return functionName + "()"
    }
}
