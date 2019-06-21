package com.codebrig.arthur.observe.structure

import com.codebrig.arthur.SourceNode
import org.apache.commons.lang.StringEscapeUtils
import org.apache.commons.lang.math.NumberUtils

/**
 * Used to determine and get the literal type of UAST nodes
 *
 * @version 0.4
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
abstract class StructureLiteral {

    long toLong(String value) {
        value = value.replace("_", "")
        try {
            if (value.toUpperCase().startsWith("0X") && value.toUpperCase().endsWith("L")) {
                return new BigInteger(value.substring(2, value.length() - 1), 16).longValue()
            }
            if (value.toUpperCase().endsWith("L")) {
                return Long.decode(value.substring(0, value.length() - 1))
            }
            return Long.valueOf(value)
        } catch (Exception ex) {
            return NumberUtils.toLong(value)
        }
    }

    double toDouble(String value) {
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

    Object getNodeLiteralValue(SourceNode node) {
        switch (node.getLiteralAttribute()) {
            case booleanValueLiteral():
                return Boolean.valueOf(node.token)
            case numberValueLiteral():
                return toLong(node.token)
            case doubleValueLiteral():
                return toDouble(node.token)
            default:
                return StringEscapeUtils.escapeJava(node.token) //treat as string
        }
    }

    abstract String getNodeLiteralAttribute(SourceNode node)

    abstract List<String> getPossibleNodeLiteralAttributes(SourceNode node)

    static String numberValueLiteral() {
        return "numberValue"
    }

    static String doubleValueLiteral() {
        return "doubleValue"
    }

    static String booleanValueLiteral() {
        return "booleanValue"
    }

    static Map<String, String> getAllLiteralAttributes() {
        def rtnMap = new LinkedHashMap<String, String>()
        rtnMap.put(booleanValueLiteral(), "boolean")
        rtnMap.put(doubleValueLiteral(), "double")
        rtnMap.put("name", "string")
        rtnMap.put(numberValueLiteral(), "long")
        rtnMap.put("token", "string")
        return rtnMap
    }
}
