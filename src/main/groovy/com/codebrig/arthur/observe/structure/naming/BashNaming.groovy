package com.codebrig.arthur.observe.structure.naming

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureNaming

/**
 * Used to get the names/qualified names of Bash AST nodes
 *
 * @version 0.4
 * @since 0.4
 * @author <a href="mailto:valpecaoco@gmail.com">Val Pecaoco</a>
 */
class BashNaming implements StructureNaming {

    @Override
    boolean isNamedNodeType(String internalType) {
        switch (Objects.requireNonNull(internalType)) {
            case "function-def-element":
                return true
            default:
                return false
        }
    }

    @Override
    String getNodeName(SourceNode node) {
        switch (Objects.requireNonNull(node).internalType) {
            case "function-def-element":
                return getFunctionDefElementName(node)
            default:
                throw new IllegalArgumentException("Unsupported Bash node type: " + node.internalType)
        }
    }

    static String getFunctionDefElementName(SourceNode node) {
        return node.name + "()"
    }
}
