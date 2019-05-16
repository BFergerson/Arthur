package com.codebrig.arthur.observe.structure.filter.operator.relational

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.RoleFilter
import com.codebrig.arthur.observe.structure.filter.TypeFilter
import com.google.common.collect.Sets

/**
 * Match by not equal operator
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class NotEqualOperatorFilter extends StructureFilter<EqualOperatorFilter, Void> {

    private final MultiFilter filter

    private final Set<Integer> notEqualNodeIdentities = Sets.newConcurrentHashSet()

    NotEqualOperatorFilter() {
        filter = MultiFilter.matchAll(
                new RoleFilter("NOT"), new RoleFilter("EQUAL"),
                new RoleFilter("OPERATOR"), new RoleFilter("RELATIONAL")
        )
    }

    @Override
    boolean evaluate(SourceNode node) {
        boolean result = filter.evaluate(node)
        if (result) {
            MultiFilter.matchAll(
                    new TypeFilter("NotEq", "Expr_BinaryOp_NotEqual", "Operator")
            ).getFilteredNodes(node).each {
                notEqualNodeIdentities.add(System.identityHashCode(it.underlyingNode))
            }
        }

        int nodeIdentity = System.identityHashCode(node.underlyingNode)
        if (notEqualNodeIdentities.contains(nodeIdentity)) {
            notEqualNodeIdentities.remove(nodeIdentity)
            return true
        }
        return false
    }
}
