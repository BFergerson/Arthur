package com.codebrig.arthur.observe.structure.filter.exception

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter
import com.codebrig.arthur.observe.structure.filter.InternalRoleFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.RoleFilter
import com.codebrig.arthur.observe.structure.filter.TypeFilter

/**
 * Match by catch in exception handling construct
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class CatchFilter extends StructureFilter<CatchFilter, Void> {

    private final MultiFilter filter

    CatchFilter() {
        filter = MultiFilter.matchAny(
                MultiFilter.matchAll(
                        new RoleFilter("TRY"), new RoleFilter("CATCH")
                ),
                new InternalRoleFilter("CatchKeyword")
        )
    }

    @Override
    boolean evaluate(SourceNode node) {
        boolean result = filter.evaluate(node)
        if (result) {
            def matched = MultiFilter.matchAll(
                    new RoleFilter("TRY", "CATCH", "STATEMENT"),
                    new TypeFilter("TryExcept")
            ).getFilteredNodes(node)
            return !matched.hasNext()
        }
        return result
    }
}
