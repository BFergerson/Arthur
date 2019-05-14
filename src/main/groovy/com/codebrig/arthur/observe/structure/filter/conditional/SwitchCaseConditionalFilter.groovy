package com.codebrig.arthur.observe.structure.filter.conditional

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.RoleFilter

/**
 * Match by case in switch conditional
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class SwitchCaseConditionalFilter extends StructureFilter<SwitchCaseConditionalFilter, Void> {

    private final MultiFilter filter

    SwitchCaseConditionalFilter() {
        this.filter = MultiFilter.matchAll(
                new RoleFilter("CASE"), new RoleFilter("SWITCH", "STATEMENT"),
                new RoleFilter().reject("EXPRESSION", "LITERAL", "NUMBER", "CONDITION", "BODY")
        )
    }

    @Override
    boolean evaluate(SourceNode node) {
        return this.filter.evaluate(node)
    }
}
