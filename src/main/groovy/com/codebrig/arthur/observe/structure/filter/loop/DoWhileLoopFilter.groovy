package com.codebrig.arthur.observe.structure.filter.loop

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.RoleFilter

/**
 * Match by do while loop
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 * @author <a href="mailto:valpecaoco@gmail.com">Val Pecaoco</a>
 */
class DoWhileLoopFilter extends StructureFilter<DoWhileLoopFilter, Void> {

    private final MultiFilter filter

    DoWhileLoopFilter() {
        filter = MultiFilter.matchAny(
                MultiFilter.matchAll(
                        new RoleFilter("DO_WHILE"), new RoleFilter("STATEMENT"),
                        new RoleFilter().reject("BLOCK", "SCOPE", "BODY")
                )
        )
    }

    @Override
    boolean evaluate(SourceNode node) {
        return filter.evaluate(node)
    }
}
