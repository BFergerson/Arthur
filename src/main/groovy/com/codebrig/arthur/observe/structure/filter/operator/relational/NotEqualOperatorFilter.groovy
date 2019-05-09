package com.codebrig.arthur.observe.structure.filter.operator.relational

import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.RoleFilter

/**
 * Match by not equal operator
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class NotEqualOperatorFilter extends StructureFilter<EqualOperatorFilter, Void> {

    private final MultiFilter notEqualOperatorFilter

    NotEqualOperatorFilter() {
        MultiFilter notEqualToken1Filter = MultiFilter.matchAll(
                new RoleFilter("NOT"), new RoleFilter("EQUAL"), new RoleFilter("OPERATOR"), new RoleFilter("RELATIONAL"),
                new RoleFilter("EXPRESSION"), new RoleFilter("BINARY"),
                new RoleFilter("IF"), new RoleFilter("CONDITION")
        )
        MultiFilter notEqualToken2Filter = MultiFilter.matchAll(
                new RoleFilter("NOT"), new RoleFilter("EQUAL"), new RoleFilter("OPERATOR"), new RoleFilter("RELATIONAL"),
                new RoleFilter("EXPRESSION"), new RoleFilter("BINARY")
        )
        MultiFilter notEqualToken3Filter = MultiFilter.matchAll(
                new RoleFilter("NOT"), new RoleFilter("EQUAL"), new RoleFilter("OPERATOR"), new RoleFilter("RELATIONAL")
        )
        this.notEqualOperatorFilter = MultiFilter.matchAny(notEqualToken1Filter, notEqualToken2Filter, notEqualToken3Filter)
    }

    @Override
    boolean evaluate(SourceNode node) {
        boolean result = this.notEqualOperatorFilter.evaluate(node)
        if (result) {
            if (node.language == SourceLanguage.Php) {
                return true
            } else {
                return node.token == "!="
            }
        }
        return false
    }
}
