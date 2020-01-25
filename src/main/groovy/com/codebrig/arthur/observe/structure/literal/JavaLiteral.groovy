package com.codebrig.arthur.observe.structure.literal

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureLiteral

/**
 * Used to determine and get the literal type of Java AST nodes
 *
 * @version 0.4
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 * @author <a href="mailto:valpecaoco@gmail.com">Val Pecaoco</a>
 */
class JavaLiteral extends StructureLiteral {

    @Override
    String getNodeLiteralAttribute(SourceNode node) {
        switch (Objects.requireNonNull(node).internalType) {
            case "NumberLiteral":
                if (node.token.contains(".") ||
                        (node.token.isDouble() &&
                                (node.token.toUpperCase().contains("P")
                                 || node.token.toUpperCase().contains("E")
                                 || node.token.toUpperCase().endsWith("D")
                                 || node.token.toUpperCase().endsWith("F"))
                        )) {
                    return doubleValueLiteral()
                }
                return numberValueLiteral()
            case "StringLiteral":
                return stringValueLiteral()
            default:
                return null
        }
    }

    @Override
    List<String> getPossibleNodeLiteralAttributes(SourceNode node) {
        switch (Objects.requireNonNull(node).internalType) {
            case "NumberLiteral":
                return [numberValueLiteral(), doubleValueLiteral()]
            case "StringLiteral":
                return [stringValueLiteral()]
            default:
                return Collections.emptyList()
        }
    }
}
