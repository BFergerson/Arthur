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
        def name = node.token
        if (name.isEmpty()) {
            name = node.properties.get("Text")
        }
        boolean isNegative = isNodeLiteralNegative(node)
        switch (node.getLiteralAttribute()) {
            case numberValueLiteral():
                return toLong(((isNegative) ? "-" : "") + name)
            case doubleValueLiteral():
                return toDouble(((isNegative) ? "-" : "") + name)
            default:
                return StringEscapeUtils.escapeJava(name) //treat as string
        }
    }
}
