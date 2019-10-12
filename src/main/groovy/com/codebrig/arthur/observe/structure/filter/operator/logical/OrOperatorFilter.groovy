package com.codebrig.arthur.observe.structure.filter.operator.logical

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter
import com.codebrig.arthur.observe.structure.filter.InternalRoleFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.RoleFilter
import com.codebrig.arthur.observe.structure.filter.TypeFilter

/**
 * Match by logical or operator
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 * @author <a href="mailto:valpecaoco@gmail.com">Val Pecaoco</a>
 */
class OrOperatorFilter extends StructureFilter<OrOperatorFilter, Void> {

    private final MultiFilter filter

    OrOperatorFilter() {
        filter = MultiFilter.matchAny(
                MultiFilter.matchAll(
                        new RoleFilter("OR"), new RoleFilter("OPERATOR"), new RoleFilter("BOOLEAN", "RELATIONAL"),
                        new RoleFilter().reject("IF", "CONDITION")
                ),
                MultiFilter.matchAll(
                        new RoleFilter("OR"), new RoleFilter("OPERATOR"), new RoleFilter("BOOLEAN"),
                        new RoleFilter("BINARY"), new RoleFilter("EXPRESSION"), new RoleFilter("CONDITION"),
                        new TypeFilter("or"),
                        new InternalRoleFilter("condition")
                )
        )
    }

    @Override
    boolean evaluate(SourceNode node) {
        return filter.evaluate(node)
    }
}
