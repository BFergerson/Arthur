package com.codebrig.omnisrc.observe.structure

import com.codebrig.omnisrc.SourceNode

/**
 * Used to get the names/qualified names of UAST nodes
 *
 * @version 0.3
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
trait StructureNaming {
    boolean isNamedNodeType(SourceNode node) {
        return isNamedNodeType(node.internalType)
    }

    abstract boolean isNamedNodeType(String internalType)

    abstract String getNodeName(SourceNode node)
}
