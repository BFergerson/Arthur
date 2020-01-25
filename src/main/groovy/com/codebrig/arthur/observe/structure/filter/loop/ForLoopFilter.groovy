package com.codebrig.arthur.observe.structure.filter.loop

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.RoleFilter

/**
 * Match by for loop
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class ForLoopFilter extends StructureFilter<ForLoopFilter, Void> {

    private final MultiFilter filter

    ForLoopFilter() {
        filter = MultiFilter.matchAll(
                new RoleFilter("FOR"), new RoleFilter("STATEMENT"),
                new RoleFilter().reject("BLOCK", "SCOPE", "BODY", "DECLARATION", "INCREMENT", "UNARY", "UPDATE")
        )
    }

    @Override
    boolean evaluate(SourceNode node) {
        return filter.evaluate(node)
    }
}
