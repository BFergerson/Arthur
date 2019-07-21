package com.codebrig.arthur.observe.structure.literal

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureLiteral

/**
 * Used to determine and get the literal type of Php AST nodes
 *
 * @version 0.4
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 * @author <a href="mailto:valpecaoco@gmail.com">Val Pecaoco</a>
 */
class PhpLiteral extends StructureLiteral {

    @Override
    String getNodeLiteralAttribute(SourceNode node) {
        switch (Objects.requireNonNull(node).internalType) {
            case "Scalar_LNumber":
                return numberValueLiteral()
            case "Scalar_DNumber":
                return doubleValueLiteral()
            case "Scalar_String":
                return stringValueLiteral()
            default:
                return null
        }
    }

    @Override
    List<String> getPossibleNodeLiteralAttributes(SourceNode node) {
        switch (Objects.requireNonNull(node).internalType) {
            case "Scalar_LNumber":
                return [numberValueLiteral()]
            case "Scalar_DNumber":
                return [doubleValueLiteral()]
            case "StringLiteral":
                return [stringValueLiteral()]
            default:
                return Collections.emptyList()
        }
    }

    @Override
    boolean isNegative(SourceNode node) {
        return node.parentSourceNode.any { it.internalType == "Expr_UnaryMinus" }
    }
}
