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

    private final MultiFilter switchCaseConditionalFilter

    SwitchCaseConditionalFilter() {
        super()
        this.switchCaseConditionalFilter = createSwitchCaseConditionalFilter()
    }

    private static createSwitchCaseConditionalFilter() {
        MultiFilter switchStatementFilter = MultiFilter.matchAll(
                new RoleFilter("SWITCH"), new RoleFilter("CASE"),
                new RoleFilter().reject("EXPRESSION", "LITERAL", "NUMBER", "CONDITION", "BODY")
        )
        MultiFilter switchClauseFilter = MultiFilter.matchAll(
                new RoleFilter("CASE"), new RoleFilter("STATEMENT"),
                new RoleFilter().reject("BODY")
        )
        return MultiFilter.matchAny(switchStatementFilter, switchClauseFilter)
    }

    @Override
    boolean evaluate(SourceNode node) {
        return this.switchCaseConditionalFilter.evaluate(node)
    }
}
