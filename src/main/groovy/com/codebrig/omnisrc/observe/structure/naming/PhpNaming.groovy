package com.codebrig.omnisrc.observe.structure.naming

import com.codebrig.omnisrc.SourceNode
import com.codebrig.omnisrc.observe.structure.StructureNaming

/**
 * Used to get the names of PHP AST nodes
 *
 * @version 0.3.1
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class PhpNaming implements StructureNaming {

    @Override
    boolean isNamedNodeType(String internalType) {
        return false
    }

    @Override
    String getNodeName(SourceNode node) {
        return null
    }
}
