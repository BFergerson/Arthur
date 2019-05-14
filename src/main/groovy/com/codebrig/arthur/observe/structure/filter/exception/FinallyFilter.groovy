package com.codebrig.arthur.observe.structure.filter.exception

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter
import com.codebrig.arthur.observe.structure.filter.InternalRoleFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.RoleFilter

/**
 * Match by finally in exception handling construct
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class FinallyFilter extends StructureFilter<FinallyFilter, Void> {

    private final MultiFilter filter

    FinallyFilter() {
        this.filter = MultiFilter.matchAll(
                new RoleFilter("TRY"), new RoleFilter("STATEMENT"),
                new RoleFilter().reject("CATCH")
        )
    }

    @Override
    boolean evaluate(SourceNode node) {
        boolean result = this.filter.evaluate(node)
        if (result) {
            def matched = MultiFilter.matchAll(
                    new InternalRoleFilter("finally", "finalizer", "finalbody")
            ).getFilteredNodes(node.children)
            return matched.hasNext()
        }
        return result
    }
}
