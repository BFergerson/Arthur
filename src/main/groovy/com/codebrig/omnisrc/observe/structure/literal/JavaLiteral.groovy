package com.codebrig.omnisrc.observe.structure.literal

import com.codebrig.omnisrc.SourceNode
import com.codebrig.omnisrc.observe.structure.StructureLiteral

/**
 * Used to determine and get the literal type of Java AST nodes
 *
 * @version 0.3
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class JavaLiteral extends StructureLiteral {

    @Override
    String getNodeLiteralAttribute(SourceNode node) {
        switch (Objects.requireNonNull(node).internalType) {
            case "NumberLiteral":
                if (node.token.contains(".")
                        || node.token.toUpperCase().contains("P")
                        || node.token.toUpperCase().contains("E")
                        || node.token.toUpperCase().endsWith("D")
                        || node.token.toUpperCase().endsWith("F")) {
                    return floatValueLiteral()
                }
                return numberValueLiteral()
            default:
                return null
        }
    }

    @Override
    List<String> getPossibleNodeLiteralAttributes(SourceNode node) {
        switch (Objects.requireNonNull(node).internalType) {
            case "NumberLiteral":
                return [numberValueLiteral(), floatValueLiteral()]
            default:
                return Collections.emptyList()
        }
    }
}
