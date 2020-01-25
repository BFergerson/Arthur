package com.codebrig.arthur.observe.structure

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.filter.TypeFilter

/**
 * Used to get the names/qualified names of UAST nodes
 *
 * @version 0.4
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
abstract class StructureNaming {

    boolean isNamedNodeType(SourceNode node) {
        def internalType = node.internalType
        switch (internalType) {
            case "uast:FunctionGroup":
                return true
        }
        return isNamedNodeType(internalType)
    }

    abstract boolean isNamedNodeType(String internalType)

    String getNodeName(SourceNode node) {
        switch (node.internalType) {
            case "uast:FunctionGroup":
                return new TypeFilter("uast:Identifier").getFilteredNodes(node).next().getName()
            default:
                throw new IllegalArgumentException("Unsupported node type: " + node.internalType)
        }
    }
}
