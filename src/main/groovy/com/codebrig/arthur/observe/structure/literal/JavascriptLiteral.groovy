package com.codebrig.arthur.observe.structure.literal

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureLiteral

/**
 * Used to determine and get the literal type of Javascript AST nodes
 *
 * @version 0.3.2
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class JavascriptLiteral extends StructureLiteral {

    @Override
    String getNodeLiteralAttribute(SourceNode node) {
        switch (Objects.requireNonNull(node).internalType) {
            case "NumericLiteral":
                return numberValueLiteral()
            default:
                return null
        }
    }

    @Override
    List<String> getPossibleNodeLiteralAttributes(SourceNode node) {
        def literalAttribute = getNodeLiteralAttribute(node)
        if (literalAttribute == null) {
            return Collections.emptyList()
        } else {
            return Collections.singletonList(literalAttribute)
        }
    }
}
