package com.codebrig.arthur.observe.structure.literal

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureLiteral
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Used to determine and get the literal type of Go AST nodes
 *
 * @version 0.4
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class GoLiteral extends StructureLiteral {

    private static final Logger log = LoggerFactory.getLogger(this.name)

    @Override
    String getNodeLiteralAttribute(SourceNode node) {
        switch (Objects.requireNonNull(node).internalType) {
            case "BasicLit":
                if (node.properties.get("Kind") == "STRING" || node.properties.get("Kind") == "CHAR") {
                    return null
                } else if (node.properties.get("Kind") == "INT") {
                    return numberValueLiteral()
                } else if (node.properties.get("Kind") == "FLOAT") {
                    return doubleValueLiteral()
                } else if (node.properties.get("Kind") == "IMAG") {
                    log.warn("Unsupported node literal: IMAG. Skipping...")
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
                return [numberValueLiteral(), doubleValueLiteral()]
            default:
                return Collections.emptyList()
        }
    }
}
