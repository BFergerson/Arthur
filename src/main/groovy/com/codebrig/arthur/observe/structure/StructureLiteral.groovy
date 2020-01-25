package com.codebrig.arthur.observe.structure

import com.codebrig.arthur.SourceNode
import org.apache.commons.text.StringEscapeUtils
import org.apache.commons.lang3.math.NumberUtils

/**
 * Used to determine and get the literal type of UAST nodes
 *
 * @version 0.4
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 * @author <a href="mailto:valpecaoco@gmail.com">Val Pecaoco</a>
 */
abstract class StructureLiteral {

    static long toLong(String value) {
        value = value.replace("_", "")
        try {
            if (value.toUpperCase().startsWith("0X") || value.toUpperCase().startsWith("-0X")) {
                int p = 2
                if (value.toUpperCase().startsWith("-0X")) {
                    p = 3
                }
                int s = 0
                if (value.toUpperCase().endsWith("L")) {
                    s = 1
                }
                String v = extractIfNegativeLiteral(value, "-0X", p, s)
                return new BigInteger(v, 16).longValue()
            } else if (value.toUpperCase().startsWith("0B") || value.toUpperCase().startsWith("-0B")) {
                int p = 2
                if (value.toUpperCase().startsWith("-0B")) {
                    p = 3
                }
                int s = 0
                if (value.toUpperCase().endsWith("L")) {
                    s = 1
                }
                String v = extractIfNegativeLiteral(value, "-0B", p, s)
                return new BigInteger(v, 2).longValue()
            } else if (value.startsWith("0") || value.startsWith("-0")) {
                if (value.matches(/0[1-7]*(l|L)?/)) {
                    int p = 1
                    if (value.toUpperCase().startsWith("-0")) {
                        p = 2
                    }
                    int s = 0
                    if (value.toUpperCase().endsWith("L")) {
                        s = 1
                    }
                    String v = extractIfNegativeLiteral(value, "-0B", p, s)
                    return new BigInteger(v, 8).longValue()
                }
            }
            if (value.toUpperCase().endsWith("L")) {
                return Long.decode(value.substring(0, value.length() - 1))
            }
            return Long.valueOf(value)
        } catch (Exception ex) {
            return NumberUtils.toLong(value)
        }
    }

    static double toDouble(String value) {
        value = value.replace("_", "")
        try {
            return Double.valueOf(value)
        } catch (Exception ex) {
            return NumberUtils.toDouble(value)
        }
    }

    boolean isNodeLiteral(SourceNode node) {
        return getNodeLiteralAttribute(node) != null
    }

    boolean isNodeLiteralNegative(SourceNode node) {
        return node.parentSourceNode.children.any { it.roles.any { it.negative } }
    }

    Object getNodeLiteralValue(SourceNode node) {
        boolean isNegative = isNodeLiteralNegative(node)
        switch (node.getLiteralAttribute()) {
            case numberValueLiteral():
                return toLong(((isNegative) ? "-" : "") + node.token)
            case doubleValueLiteral():
                return toDouble(((isNegative) ? "-" : "") + node.token)
            default:
                return StringEscapeUtils.escapeJava(node.token) //treat as string
        }
    }

    abstract String getNodeLiteralAttribute(SourceNode node)

    abstract List<String> getPossibleNodeLiteralAttributes(SourceNode node)

    static String booleanValueLiteral() {
        return "booleanValue"
    }

    static String doubleValueLiteral() {
        return "doubleValue"
    }

    static String numberValueLiteral() {
        return "numberValue"
    }

    static String stringValueLiteral() {
        return "stringValue"
    }

    static Map<String, String> getAllLiteralAttributes() {
        def rtnMap = new LinkedHashMap<String, String>()
        rtnMap.put(booleanValueLiteral(), "boolean")
        rtnMap.put(doubleValueLiteral(), "double")
        rtnMap.put("name", "string")
        rtnMap.put(numberValueLiteral(), "long")
        rtnMap.put(stringValueLiteral(), "string")
        rtnMap.put("token", "string")
        return rtnMap
    }

    static String extractIfNegativeLiteral(String value, String base, int p, int s) {
        String v = value.substring(p, value.length() - s)
        v = value.toUpperCase().startsWith(base) ? "-" + v : v
        return v
    }
}
