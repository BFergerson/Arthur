package com.codebrig.arthur.observe.structure.filter.operator.relational

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.RoleFilter
import com.codebrig.arthur.observe.structure.filter.TypeFilter
import com.google.common.collect.Sets

/**
 * Match by not equal type operator
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class NotEqualTypeOperatorFilter extends StructureFilter<NotEqualTypeOperatorFilter, Void> {

    private final MultiFilter filter

    private final Set<Integer> notEqualTypeNodeIdentities = Sets.newConcurrentHashSet()

    NotEqualTypeOperatorFilter() {
        MultiFilter notEqualTypeToken1Filter = MultiFilter.matchAll(
                new RoleFilter("NOT"), new RoleFilter("IDENTICAL"), new RoleFilter("OPERATOR"), new RoleFilter("RELATIONAL"),
                new RoleFilter("EXPRESSION"), new RoleFilter("BINARY")
        )
        MultiFilter notEqualTypeToken2Filter = MultiFilter.matchAll(notEqualTypeToken1Filter,
                new RoleFilter("IF"), new RoleFilter("CONDITION")
        )
        filter = MultiFilter.matchAny(notEqualTypeToken1Filter, notEqualTypeToken2Filter)
    }

    @Override
    boolean evaluate(SourceNode node) {
        boolean result = filter.evaluate(node)
        if (result) {
            MultiFilter.matchAll(
                    new TypeFilter("Expr_BinaryOp_NotIdentical", "Operator")
            ).getFilteredNodes(node).each {
                notEqualTypeNodeIdentities.add(System.identityHashCode(it.underlyingNode))
            }
        }

        int nodeIdentity = System.identityHashCode(node.underlyingNode)
        if (notEqualTypeNodeIdentities.contains(nodeIdentity)) {
            notEqualTypeNodeIdentities.remove(nodeIdentity)
            return true
        }
        return false
    }
}
