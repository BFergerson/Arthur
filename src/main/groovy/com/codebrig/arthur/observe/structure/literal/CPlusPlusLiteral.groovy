package com.codebrig.arthur.observe.structure.literal

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureLiteral
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.RoleFilter
import org.apache.commons.text.StringEscapeUtils

/**
 * Used to determine and get the literal type of C++ AST nodes
 *
 * @version 0.4
 * @since 0.4
 * @author <a href="mailto:valpecaoco@gmail.com">Val Pecaoco</a>
 */
class CPlusPlusLiteral extends StructureLiteral {

    @Override
    String getNodeLiteralAttribute(SourceNode node) {
        switch (Objects.requireNonNull(node).internalType) {
            case "CPPASTLiteralExpression":
                def matchedNumber = MultiFilter.matchAll(
                        new RoleFilter("NUMBER"), new RoleFilter("LITERAL"), new RoleFilter("EXPRESSION")
                ).getFilteredNodes(node)
                if (matchedNumber.hasNext()) {
                    if (node.token.contains(".") ||
                            (node.token.isDouble() &&
                                    (node.token.toUpperCase().contains("P")
                                    || node.token.toUpperCase().contains("E")
                                    || node.token.toUpperCase().endsWith("F"))
                            )) {
                        return doubleValueLiteral()
                    }
                    return numberValueLiteral()
                }
                def matchedString = MultiFilter.matchAll(
                        new RoleFilter("STRING"), new RoleFilter("LITERAL"), new RoleFilter("EXPRESSION")
                ).getFilteredNodes(node)
                if (matchedString.hasNext()) {
                    return stringValueLiteral()
                }
                return null
            default:
                return null
        }
    }

    @Override
    List<String> getPossibleNodeLiteralAttributes(SourceNode node) {
        switch (Objects.requireNonNull(node).internalType) {
            case "CPPASTLiteralExpression":
                def matchedNumber = MultiFilter.matchAll(
                        new RoleFilter("NUMBER"), new RoleFilter("LITERAL"), new RoleFilter("EXPRESSION")
                ).getFilteredNodes(node)
                if (matchedNumber.hasNext()) {
                    return [numberValueLiteral(), doubleValueLiteral()]
                }
                def matchedString = MultiFilter.matchAll(
                        new RoleFilter("STRING"), new RoleFilter("LITERAL"), new RoleFilter("EXPRESSION")
                ).getFilteredNodes(node)
                if (matchedString.hasNext()) {
                    return [stringValueLiteral()]
                }
                return Collections.emptyList()
            default:
                return Collections.emptyList()
        }
    }

    @Override
    Object getNodeLiteralValue(SourceNode node) {
        def value = node.token
        value = value.replace("'", "")
        boolean isNegative = isNodeLiteralNegative(node)
        switch (node.getLiteralAttribute()) {
            case numberValueLiteral():
                def uc = value.toUpperCase()
                if (uc.endsWith("U") || uc.endsWith("UL") || uc.endsWith("LU") || uc.endsWith("LL") ||
                    uc.endsWith("ULL") || uc.endsWith("LLU")) {
                    int i = 1
                    if (uc.endsWith("UL") || uc.endsWith("LU") || uc.endsWith("LL")) {
                        i = 2
                    }
                    if (uc.endsWith("ULL") || uc.endsWith("LLU")) {
                        i = 3
                    }
                    long ul = Long.parseUnsignedLong(value.substring(0, value.length() - i))
                    return (((isNegative) ? "-" : "") + Long.toUnsignedString(ul))
                }
                return toLong(((isNegative) ? "-" : "") + value)
            case doubleValueLiteral():
                if (value.toUpperCase().endsWith("L")) {
                    return (((isNegative) ? "-" : "") + Double.valueOf(value.substring(0, value.length() - 1)))
                }
                return toDouble(((isNegative) ? "-" : "") + value)
            default:
                return StringEscapeUtils.escapeJava(value) //treat as string
        }
    }

    @Override
    boolean isNodeLiteralNegative(SourceNode node) {
        return node.parentSourceNode.properties.get("operator") == "op_minus"
    }
}
