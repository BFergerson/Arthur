package com.codebrig.omnisrc.observe.structure.literal

import com.codebrig.omnisrc.SourceNode
import com.codebrig.omnisrc.observe.structure.StructureLiteral

/**
 * Used to determine and get the literal type of Go AST nodes
 *
 * @version 0.3
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class GoLiteral extends StructureLiteral {

    @Override
    String getNodeLiteralAttribute(SourceNode node) {
        switch (Objects.requireNonNull(node).internalType) {
            case "BasicLit":
                if (node.properties.get("Kind") == "STRING" || node.properties.get("Kind") == "CHAR") {
                    return null
                } else if (node.properties.get("Kind") == "INT") {
                    return numberValueLiteral()
                } else if (node.properties.get("Kind") == "FLOAT") {
                    return floatValueLiteral()
                } else {
                    throw new UnsupportedOperationException("Literal kind: " + node.properties.get("Kind"))
                }
        }
        return null
    }

    @Override
    List<String> getPossibleNodeLiteralAttributes(SourceNode node) {
        switch (Objects.requireNonNull(node).internalType) {
            case "BasicLit":
                return [numberValueLiteral(), floatValueLiteral()]
            default:
                return Collections.emptyList()
        }
    }
}
