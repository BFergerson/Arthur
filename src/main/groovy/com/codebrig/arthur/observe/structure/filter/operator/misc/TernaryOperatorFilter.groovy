package com.codebrig.arthur.observe.structure.filter.operator.misc

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter
import com.codebrig.arthur.observe.structure.filter.InternalRoleFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.RoleFilter

/**
 * Match by ternary operator
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 * @author <a href="mailto:valpecaoco@gmail.com">Val Pecaoco</a>
 */
class TernaryOperatorFilter extends StructureFilter<TernaryOperatorFilter, Void> {

    private final MultiFilter filter

    TernaryOperatorFilter() {
        filter = MultiFilter.matchAny(
                MultiFilter.matchAll(
                        new RoleFilter("IF"), new RoleFilter("EXPRESSION"),
                        new RoleFilter("ASSIGNMENT"), new RoleFilter("BINARY"),
                        new RoleFilter("RIGHT")
                ),
                MultiFilter.matchAll(
                        new RoleFilter("CONDITION"), new RoleFilter("EXPRESSION"),
                        new InternalRoleFilter().reject("Prop_InitOperand2")
                )
        )
    }

    @Override
    boolean evaluate(SourceNode node) {
        if (Objects.requireNonNull(node).internalType == "ConditionalExpression") {
            return evaluateConditionalExpression(node)
        } else {
            return filter.evaluate(node)
        }
    }

    static boolean evaluateConditionalExpression(SourceNode node) {
        if (node.children.any { it.internalType == "QuestionToken" }) {
            if (node.children.any { it.internalType == "ColonToken" }) {
                return true
            }
        }
        return false
    }
}
