package com.codebrig.arthur.observe.structure.filter.operator.logical

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.RoleFilter

/**
 * Match by logical and operator
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class AndOperatorFilter extends StructureFilter<AndOperatorFilter, Void> {

    private final MultiFilter andOperatorFilter

    AndOperatorFilter() {
        super()
        this.andOperatorFilter = createAndOperatorFilter()
    }

    private static createAndOperatorFilter() {
        MultiFilter andToken1Filter = MultiFilter.matchAll(
                new RoleFilter("AND"), new RoleFilter("OPERATOR"), new RoleFilter("BOOLEAN"),
                new RoleFilter("EXPRESSION"), new RoleFilter("BINARY"),
                new RoleFilter().reject("IF", "CONDITION")
        )
        MultiFilter andToken2Filter = MultiFilter.matchAll(
                new RoleFilter("AND"), new RoleFilter("OPERATOR"), new RoleFilter("BOOLEAN"),
                new RoleFilter().reject("IF", "CONDITION")
        )
        return MultiFilter.matchAny(andToken1Filter, andToken2Filter)
    }

    @Override
    boolean evaluate(SourceNode node) {
        return this.andOperatorFilter.evaluate(node)
    }
}
