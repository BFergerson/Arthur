package com.codebrig.arthur.observe.structure.filter.conditional

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter
import com.codebrig.arthur.observe.structure.filter.InternalRoleFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.RoleFilter
import com.codebrig.arthur.observe.structure.filter.TypeFilter

/**
 * Match by case in switch conditional
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 * @author <a href="mailto:valpecaoco@gmail.com">Val Pecaoco</a>
 */
class SwitchCaseConditionalFilter extends StructureFilter<SwitchCaseConditionalFilter, Void> {

    private final MultiFilter filter

    SwitchCaseConditionalFilter() {
        filter = MultiFilter.matchAny(
                MultiFilter.matchAll(
                        new RoleFilter("CASE"),  new RoleFilter("SWITCH", "STATEMENT"),
                        new RoleFilter().reject("EXPRESSION", "LITERAL", "NUMBER", "CONDITION", "BODY"),
                        new TypeFilter().reject("CaseSwitchLabel")
                ),
                MultiFilter.matchAll(
                        new RoleFilter("SWITCH"),  new RoleFilter("STATEMENT"),
                        new TypeFilter("case")
                ),
                MultiFilter.matchAll(
                        new InternalRoleFilter("cases"),
                        new TypeFilter("Stmt_Case")

                )
        )
    }

    @Override
    boolean evaluate(SourceNode node) {
        return filter.evaluate(node)
    }
}
