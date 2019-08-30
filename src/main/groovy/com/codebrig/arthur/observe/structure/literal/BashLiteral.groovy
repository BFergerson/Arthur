package com.codebrig.arthur.observe.structure.literal

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureLiteral

/**
 * Used to determine and get the literal type of Bash AST nodes
 *
 * @version 0.4
 * @since 0.4
 * @author <a href="mailto:valpecaoco@gmail.com">Val Pecaoco</a>
 */
class BashLiteral extends StructureLiteral {

    @Override
    String getNodeLiteralAttribute(SourceNode node) {
        switch (Objects.requireNonNull(node).internalType) {
            case "int_literal":
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
            case "string_content":
                return stringValueLiteral()
            default:
                return null
        }
    }

    @Override
    List<String> getPossibleNodeLiteralAttributes(SourceNode node) {
        switch (Objects.requireNonNull(node).internalType) {
            case "int_literal":
                return [numberValueLiteral(), doubleValueLiteral()]
            case "string_content":
                return [stringValueLiteral()]
            default:
                return Collections.emptyList()
        }
    }
}
