package com.codebrig.arthur.observe.structure.filter.operator.relational

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.RoleFilter
import com.codebrig.arthur.observe.structure.filter.TypeFilter
import com.google.common.collect.Sets

/**
 * Match by equal operator
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class EqualOperatorFilter extends StructureFilter<EqualOperatorFilter, Void> {

    private final MultiFilter filter

    private final Set<Integer> equalNodeIdentities = Sets.newConcurrentHashSet()

    EqualOperatorFilter() {
        MultiFilter equalToken1Filter = MultiFilter.matchAll(
                new RoleFilter("EQUAL"), new RoleFilter("OPERATOR"), new RoleFilter("RELATIONAL")
        )
        MultiFilter equalToken2Filter = MultiFilter.matchAll(equalToken1Filter,
                new RoleFilter("EXPRESSION"), new RoleFilter("BINARY")
        )
        MultiFilter equalToken3Filter = MultiFilter.matchAll(equalToken1Filter, equalToken2Filter,
                new RoleFilter("IF"), new RoleFilter("CONDITION")
        )
        filter = MultiFilter.matchAny(equalToken1Filter, equalToken2Filter, equalToken3Filter)
    }

    @Override
    boolean evaluate(SourceNode node) {
        boolean result = filter.evaluate(node)
        if (result) {
            MultiFilter.matchAll(
                    new TypeFilter("Eq", "Expr_BinaryOp_Equal", "Operator")
            ).getFilteredNodes(node).each {
                equalNodeIdentities.add(System.identityHashCode(it.underlyingNode))
            }
        }

        int nodeIdentity = System.identityHashCode(node.underlyingNode)
        if (equalNodeIdentities.contains(nodeIdentity)) {
            equalNodeIdentities.remove(nodeIdentity)
            return true
        }
        return false
    }
}
