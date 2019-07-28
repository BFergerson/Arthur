package com.codebrig.arthur.observe.structure.naming

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureNaming

/**
 * Used to get the names/qualified names of C# AST nodes
 *
 * @version 0.4
 * @since 0.4
 * @author <a href="mailto:valpecaoco@gmail.com">Val Pecaoco</a>
 */
class CPlusPlusNaming implements StructureNaming {

    @Override
    boolean isNamedNodeType(String internalType) {
        return false
    }

    @Override
    String getNodeName(SourceNode node) {
        return null
    }
}
