package com.codebrig.arthur.observe.structure.filter.operator.misc

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter
import com.codebrig.arthur.observe.structure.filter.InternalRoleFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.RoleFilter

/**
 * Match by Elvis operator
 *
 * @version 0.4
 * @since 0.4
 * @author <a href="mailto:valpecaoco@gmail.com">Val Pecaoco</a>
 */
class ElvisOperatorFilter extends StructureFilter<ElvisOperatorFilter, Void> {

    private final MultiFilter filter

    ElvisOperatorFilter() {
        filter = MultiFilter.matchAll(
                new RoleFilter("IF"), new RoleFilter("EXPRESSION"), new RoleFilter("RIGHT")
        )
    }

    @Override
    boolean evaluate(SourceNode node) {
        boolean result = filter.evaluate(node)
        if (result) {
            def matched = MultiFilter.matchAll(
                    new RoleFilter("THEN"),
                    new InternalRoleFilter("if")
            ).getFilteredNodes(node.children)
            return !matched.hasNext()
        }
        return result
    }
}
