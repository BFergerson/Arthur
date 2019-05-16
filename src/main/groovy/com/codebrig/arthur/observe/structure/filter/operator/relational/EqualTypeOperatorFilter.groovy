package com.codebrig.arthur.observe.structure.filter.operator.relational

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.RoleFilter
import com.codebrig.arthur.observe.structure.filter.TypeFilter
import com.google.common.collect.Sets

/**
 * Match by equal type operator
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class EqualTypeOperatorFilter extends StructureFilter<EqualTypeOperatorFilter, Void> {

    private final MultiFilter filter

    private final Set<Integer> equalTypeNodeIdentities = Sets.newConcurrentHashSet()

    EqualTypeOperatorFilter() {
        filter = MultiFilter.matchAll(
                new RoleFilter("IDENTICAL"), new RoleFilter("OPERATOR"),
                new RoleFilter("RELATIONAL"), new RoleFilter("EXPRESSION"),
                new RoleFilter("BINARY")
        )
    }

    @Override
    boolean evaluate(SourceNode node) {
        boolean result = filter.evaluate(node)
        if (result) {
            MultiFilter.matchAll(
                    new TypeFilter("Expr_BinaryOp_Identical", "Operator")
            ).getFilteredNodes(node).each {
                equalTypeNodeIdentities.add(System.identityHashCode(it.underlyingNode))
            }
        }

        int nodeIdentity = System.identityHashCode(node.underlyingNode)
        if (equalTypeNodeIdentities.contains(nodeIdentity)) {
            equalTypeNodeIdentities.remove(nodeIdentity)
            return true
        }
        return false
    }
}
