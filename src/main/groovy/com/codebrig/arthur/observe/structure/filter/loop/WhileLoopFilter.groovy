package com.codebrig.arthur.observe.structure.filter.loop

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter
import com.codebrig.arthur.observe.structure.filter.InternalRoleFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.RoleFilter

/**
 * Match by while loop
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class WhileLoopFilter extends StructureFilter<WhileLoopFilter, Void> {

    private final MultiFilter filter

    WhileLoopFilter() {
        this.filter = MultiFilter.matchAll(
                new RoleFilter("WHILE", "FOR"), new RoleFilter("STATEMENT"),
                new RoleFilter().reject("BLOCK", "SCOPE", "BODY")
        )
    }

    @Override
    boolean evaluate(SourceNode node) {
        boolean result = this.filter.evaluate(node)
        if (result) {
            def matched = MultiFilter.matchAll(
                    new InternalRoleFilter("Init")
            ).getFilteredNodes(node.children)
            if (matched.hasNext()) {
                return true
            }
        }
        return result
    }
}
