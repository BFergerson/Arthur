package com.codebrig.arthur.observe.structure.filter.operator.relational.compare

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.RoleFilter
import com.codebrig.arthur.observe.structure.filter.TypeFilter

/**
 * Match by is equal operator
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 * @author <a href="mailto:valpecaoco@gmail.com">Val Pecaoco</a>
 */
class IsEqualOperatorFilter extends StructureFilter<IsEqualOperatorFilter, Void> {

    private final MultiFilter filter

    IsEqualOperatorFilter() {
        filter = MultiFilter.matchAny(
                MultiFilter.matchAll(
                        new RoleFilter("EQUAL"), new RoleFilter("OPERATOR"),
                        new RoleFilter("RELATIONAL"),
                        new TypeFilter().reject("InfixExpression", "BinaryExpression")
                ),
                new TypeFilter("combined_word")
        )
    }

    @Override
    boolean evaluate(SourceNode node) {
        //todo: remove following line (https://github.com/bblfsh/cpp-driver/pull/59)
        if (node?.internalType == "CPPASTBinaryExpression" && node.token == "==") {
            return true
        } else {
            boolean result = filter.evaluate(node)
            if (result) {
                if (node.internalType == "combined_word") {
                    return evaluateCombinedWordIsEqual(node)
                }
            }
            return result
        }
    }

    static boolean evaluateCombinedWordIsEqual(SourceNode node) {
        if (node.parentSourceNode.internalType == "simple-command") {
            def isEqualToken = getIsEqualToken(node)
            if (isEqualToken == "==" || isEqualToken == "-eq") {
                return true
            }
        }
        return false
    }

    static String getIsEqualToken(SourceNode node) {
        if (node.parentSourceNode.internalType == "simple-command") {
            def op = ""
            new TypeFilter("word").getFilteredNodes(node.children).each {
                op += it.token
            }
            return (op == "==" || op == "-eq") ? op : ""
        }
        return node.token
    }
}
