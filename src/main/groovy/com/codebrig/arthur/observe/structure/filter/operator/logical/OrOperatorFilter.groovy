package com.codebrig.arthur.observe.structure.filter.operator.logical

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.RoleFilter

/**
 * Match by logical or operator
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class OrOperatorFilter extends StructureFilter<OrOperatorFilter, Void> {

    private final MultiFilter filter

    OrOperatorFilter() {
        def orToken1Filter = MultiFilter.matchAll(
                new RoleFilter("OR"), new RoleFilter("OPERATOR"), new RoleFilter("BOOLEAN"),
                new RoleFilter().reject("IF", "CONDITION")
        )
        def orToken2Filter = MultiFilter.matchAll(
                orToken1Filter, new RoleFilter("EXPRESSION"), new RoleFilter("BINARY")
        )
        filter = MultiFilter.matchAny(orToken1Filter, orToken2Filter)
    }

    @Override
    boolean evaluate(SourceNode node) {
        return filter.evaluate(node)
    }
}
