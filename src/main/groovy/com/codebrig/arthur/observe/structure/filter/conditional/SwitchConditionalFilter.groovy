package com.codebrig.arthur.observe.structure.filter.conditional

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.RoleFilter

/**
 * Match by switch conditional
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class SwitchConditionalFilter extends StructureFilter<SwitchConditionalFilter, Void> {

    private final MultiFilter filter

    SwitchConditionalFilter() {
        this.filter = MultiFilter.matchAll(
                new RoleFilter("SWITCH"), new RoleFilter("STATEMENT"),
                new RoleFilter().reject("BLOCK", "SCOPE", "BODY")
        )
    }

    @Override
    boolean evaluate(SourceNode node) {
        return this.filter.evaluate(node)
    }
}
