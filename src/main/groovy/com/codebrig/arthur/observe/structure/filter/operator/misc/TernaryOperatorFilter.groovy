package com.codebrig.arthur.observe.structure.filter.operator.misc

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.RoleFilter
import com.codebrig.arthur.observe.structure.filter.TypeFilter

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
                        new RoleFilter("IF", "CONDITION"), new RoleFilter("EXPRESSION"),
                        new RoleFilter("ASSIGNMENT", "INITIALIZATION"), new RoleFilter("BINARY"), new RoleFilter("RIGHT")
                ),
                MultiFilter.matchAll(
                        new RoleFilter("IF"), new RoleFilter("STATEMENT"),
                        new RoleFilter("BINARY"), new RoleFilter("RIGHT")
                ),
                new TypeFilter("ConditionalExpression") //todo: filter by roles
        )
    }

    @Override
    boolean evaluate(SourceNode node) {
        return filter.evaluate(node)
    }
}
