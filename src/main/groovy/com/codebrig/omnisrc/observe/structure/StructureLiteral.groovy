package com.codebrig.omnisrc.observe.structure

import com.codebrig.omnisrc.SourceNode
import org.apache.commons.lang.math.NumberUtils

/**
 * Used to determine and get the literal type of UAST nodes
 *
 * @version 0.3
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
        def rtnMap = new HashMap<String, String>()
        rtnMap.put(numberValueLiteral(), "long")
        rtnMap.put(doubleValueLiteral(), "double")
        rtnMap.put(booleanValueLiteral(), "boolean")
        return rtnMap
    }
}
