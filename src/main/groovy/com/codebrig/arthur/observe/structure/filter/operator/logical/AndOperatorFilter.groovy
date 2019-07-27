package com.codebrig.arthur.observe.structure.filter.operator.logical

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.RoleFilter

/**
 * Match by logical and operator
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 * @author <a href="mailto:valpecaoco@gmail.com">Val Pecaoco</a>
 */
class AndOperatorFilter extends StructureFilter<AndOperatorFilter, Void> {

    private final MultiFilter filter

    AndOperatorFilter() {
        filter = MultiFilter.matchAll(
                new RoleFilter("AND"), new RoleFilter("OPERATOR"), new RoleFilter("BOOLEAN", "RELATIONAL"),
                new RoleFilter().reject("IF", "CONDITION")
        )
    }

    @Override
    boolean evaluate(SourceNode node) {
        return filter.evaluate(node)
    }
}
