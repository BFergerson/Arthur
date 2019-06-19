package com.codebrig.arthur.observe.structure.filter.operator.relational

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.RoleFilter
import com.codebrig.arthur.observe.structure.filter.TypeFilter

/**
 * Match by equal type operator
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class IsEqualTypeOperatorFilter extends StructureFilter<IsEqualTypeOperatorFilter, Void> {

    private final MultiFilter filter

    IsEqualTypeOperatorFilter() {
        filter = MultiFilter.matchAll(
                new RoleFilter("IDENTICAL"), new RoleFilter("OPERATOR"),
                new RoleFilter("RELATIONAL"), new RoleFilter("EXPRESSION"),
                new RoleFilter("BINARY"),
                new TypeFilter().reject("InfixExpression", "BinaryExpression")
        )
    }

    @Override
    boolean evaluate(SourceNode node) {
        return filter.evaluate(node)
    }
}
