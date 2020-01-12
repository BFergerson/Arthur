package com.codebrig.arthur.observe.structure.filter.conditional

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter
import com.codebrig.arthur.observe.structure.filter.ChildFilter
import com.codebrig.arthur.observe.structure.filter.InternalRoleFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.RoleFilter

/**
 * Match by else if conditional
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class ElseIfConditionalFilter extends StructureFilter<ElseIfConditionalFilter, Void> {

    private final MultiFilter filter

    ElseIfConditionalFilter() {
        filter = MultiFilter.matchAny(MultiFilter.matchAll(
                new RoleFilter("IF"), new RoleFilter("ELSE"),
                new RoleFilter("STATEMENT", "BODY").reject("CALL"),
                ChildFilter.matchAll(new InternalRoleFilter(
                        "else_stmts", "thenStatement", "Cond", "Prop_ThenClause", "condition", "consequent"))
        ), new InternalRoleFilter("Else"))
    }

    @Override
    boolean evaluate(SourceNode node) {
        return filter.evaluate(node)
    }
}
