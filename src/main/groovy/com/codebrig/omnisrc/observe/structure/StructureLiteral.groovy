package com.codebrig.omnisrc.observe.structure

import com.codebrig.omnisrc.SourceNode

/**
 * Used to determine and get the literal type of UAST nodes
 *
 * @version 0.3
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
abstract class StructureLiteral {

    boolean isNodeLiteral(SourceNode node) {
        return getNodeLiteralAttribute(node) != null
    }

    abstract String getNodeLiteralAttribute(SourceNode node)

    abstract List<String> getPossibleNodeLiteralAttributes(SourceNode node)

    static String numberValueLiteral() {
        return "numberValue"
    }

    static String floatValueLiteral() {
        return "floatValue"
    }

    static String booleanValueLiteral() {
        return "booleanValue"
    }

    static Map<String, String> getAllLiteralAttributes() {
        def rtnMap = new HashMap<String, String>()
        rtnMap.put(numberValueLiteral(), "long")
        rtnMap.put(floatValueLiteral(), "double")
        rtnMap.put(booleanValueLiteral(), "boolean")
        return rtnMap
    }
}
