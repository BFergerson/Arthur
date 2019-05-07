package com.codebrig.arthur.observe.structure.filter.conditional

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.RoleFilter

/**
 * Match by if conditional
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class IfConditionalFilter extends StructureFilter<IfConditionalFilter, Void> {

    private final MultiFilter ifConditionalFilter

    IfConditionalFilter() {
        MultiFilter ifStatementFilter = MultiFilter.matchAll(
                new RoleFilter("IF"), new RoleFilter("STATEMENT"),
                new RoleFilter().reject("BLOCK", "SCOPE", "THEN", "BODY")
        )
        MultiFilter ifExpressionFilter = MultiFilter.matchAll(
                new RoleFilter("IF"), new RoleFilter("EXPRESSION"),
                new RoleFilter().reject("IDENTIFIER", "CONDITION")
        )
        this.ifConditionalFilter = MultiFilter.matchAny(ifStatementFilter, ifExpressionFilter)
    }

    @Override
    boolean evaluate(SourceNode node) {
        return this.ifConditionalFilter.evaluate(node)
    }
}
