package com.codebrig.arthur.observe.structure.filter.operator.relational.compare

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.RoleFilter
import com.codebrig.arthur.observe.structure.filter.TypeFilter

/**
 * Match by is not equal operator
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 * @author <a href="mailto:valpecaoco@gmail.com">Val Pecaoco</a>
 */
class IsNotEqualOperatorFilter extends StructureFilter<IsNotEqualOperatorFilter, Void> {

    private final MultiFilter filter

    IsNotEqualOperatorFilter() {
        filter = MultiFilter.matchAny(
                MultiFilter.matchAll(
                        new RoleFilter("NOT"), new RoleFilter("EQUAL"),
                        new RoleFilter("OPERATOR"), new RoleFilter("RELATIONAL"),
                        new TypeFilter().reject("InfixExpression", "BinaryExpression")
                ),
                new TypeFilter("combined_word")
        )
    }

    @Override
    boolean evaluate(SourceNode node) {
        boolean result = filter.evaluate(node)
        if (result) {
            if (node.internalType == "combined_word") {
                return evaluateCombinedWordIsNotEqual(node)
            }
        }
        return result
    }

    static boolean evaluateCombinedWordIsNotEqual(SourceNode node) {
        if (node.parentSourceNode.internalType == "simple-command") {
            def isNotEqualToken = getIsNotEqualToken(node)
            if (isNotEqualToken == "!=" || isNotEqualToken == "-ne") {
                return true
            }
        }
        return false
    }

    static String getIsNotEqualToken(SourceNode node) {
        if (node.parentSourceNode.internalType == "simple-command") {
            def op = ""
            new TypeFilter("word").getFilteredNodes(node.children).each {
                op += it.token
            }
            return (op == "!=" || op == "-ne") ? op : ""
        }
        return node.token
    }
}
