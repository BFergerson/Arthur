package com.codebrig.arthur.observe.structure.literal

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureLiteral

/**
 * Used to determine and get the literal type of C# AST nodes
 *
 * @version 0.4
 * @since 0.4
 * @author <a href="mailto:valpecaoco@gmail.com">Val Pecaoco</a>
 */
class CSharpLiteral extends StructureLiteral {

    @Override
    String getNodeLiteralAttribute(SourceNode node) {
        return null
    }

    @Override
    List<String> getPossibleNodeLiteralAttributes(SourceNode node) {
        return null
    }
}
