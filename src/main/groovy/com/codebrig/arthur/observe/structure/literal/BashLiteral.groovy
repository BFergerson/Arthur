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
                return numberValueLiteral()
            case "word":
                return getWordLiteralType(node)
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
                return [numberValueLiteral()]
            case "word":
                return [getWordLiteralType(node)]
            case "string_content":
                return [stringValueLiteral()]
            default:
                return Collections.emptyList()
        }
    }

    @Override
    Object getNodeLiteralValue(SourceNode node) {
        def elements = getWordElements(node)
        if (elements) {
            def radix = elements[0]
            def value = elements[1]
            return new BigInteger(value, Integer.parseInt(radix)).longValue()
        }
        return node.token
    }

    static String getWordLiteralType(SourceNode node) {
        String[] elements = getWordElements(node)
        if (elements) {
            return numberValueLiteral()
        }
        return stringValueLiteral()
    }

    static String[] getWordElements(SourceNode node) {
        if (node.internalType == "word") {
            def token = node.token.toUpperCase()
            return parseLiteralToken(token)
        }
        return null
    }

    static String[] parseLiteralToken(String token) {
        if ((token.startsWith("0") || token.startsWith("-0")) &&
                !(token.startsWith("0X") || token.startsWith("-0X"))) {
            def sign = token.startsWith("-0") ? "-" : ""
            return ["8", sign + token.substring(1 + sign.length())]
        } else if (token.startsWith("0X") || token.startsWith("-0X")) {
            def n = token.startsWith("-0X") ? "-" : ""
            return ["16", n + token.substring(2 + n.length())]
        } else if (token.contains("#")) {
            def array = token.split("#")
            if (array.size() == 2) {
                def isNegative = array[0].startsWith("-")
                def value = isNegative ? "-"+array[1] : array[1]
                def radix = isNegative ? array[0].substring(1) : array[0]
                array[0] = radix
                array[1] = value
                return array
            }
        }
        return null
    }
}
