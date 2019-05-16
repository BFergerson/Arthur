package com.codebrig.arthur.observe.structure.filter.operator.relational

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.RoleFilter
import com.codebrig.arthur.observe.structure.filter.TypeFilter

/**
 * Match by equal operator
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class EqualOperatorFilter extends StructureFilter<EqualOperatorFilter, Void> {

    private final MultiFilter filter

    EqualOperatorFilter() {
        filter = MultiFilter.matchAll(
                new RoleFilter("EQUAL"), new RoleFilter("OPERATOR"),
                new RoleFilter("RELATIONAL"),
                new TypeFilter().reject("InfixExpression", "BinaryExpression")
        )
    }

    @Override
    boolean evaluate(SourceNode node) {
        return filter.evaluate(node)
    }
}
