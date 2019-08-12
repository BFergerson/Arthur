package com.codebrig.arthur.observe.structure.literal

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureLiteral
import org.apache.commons.lang.StringEscapeUtils

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
        switch (Objects.requireNonNull(node).internalType) {
            case "NumericLiteralToken":
                String text = node.properties.get("Text")
                if (text.contains(".") ||
                        (text.isDouble() &&
                                (text.toUpperCase().endsWith("E")
                                 || text.toUpperCase().endsWith("D")
                                 || text.toUpperCase().endsWith("F"))
                        )) {
                    return doubleValueLiteral()
                }
                return numberValueLiteral()
            case "StringLiteralToken":
                return stringValueLiteral()
            default:
                return null
        }
    }

    @Override
    List<String> getPossibleNodeLiteralAttributes(SourceNode node) {
        switch (Objects.requireNonNull(node).internalType) {
            case "NumericLiteralToken":
                return [numberValueLiteral(), doubleValueLiteral()]
            case "StringLiteralToken":
                return [stringValueLiteral()]
            default:
                return Collections.emptyList()
        }
    }

    @Override
    Object getNodeLiteralValue(SourceNode node) {
        def value = node.token
        if (value.isEmpty()) {
            value = node.properties.get("Text")
        }
        boolean isNegative = isNodeLiteralNegative(node)
        switch (node.getLiteralAttribute()) {
            case numberValueLiteral():
                if (value.toUpperCase().endsWith("U") || value.toUpperCase().endsWith("UL")) {
                    int i = 1
                    if (value.toUpperCase().endsWith("UL")) {
                        i = 2
                    }
                    long ul = Long.parseUnsignedLong(value.substring(0, value.length() - i))
                    return (((isNegative) ? "-" : "") + Long.toUnsignedString(ul))
                }
                return toLong(((isNegative) ? "-" : "") + value)
            case doubleValueLiteral():
                if (value.toUpperCase().endsWith("M")) {
                    return (((isNegative) ? "-" : "") + Double.valueOf(value.substring(0, value.length() - 1)))
                }
                return toDouble(((isNegative) ? "-" : "") + value)
            default:
                return StringEscapeUtils.escapeJava(value) //treat as string
        }
    }

    @Override
    boolean isNodeLiteralNegative(SourceNode node) {
        return node.parentSourceNode.parentSourceNode.any { it.internalType == "PrefixUnaryExpression_UnaryMinusExpression" }
    }
}
