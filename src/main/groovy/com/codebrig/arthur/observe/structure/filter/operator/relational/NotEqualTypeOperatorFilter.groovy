package com.codebrig.arthur.observe.structure.filter.operator.relational

import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.RoleFilter

/**
 * Match by not equal type operator
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class NotEqualTypeOperatorFilter extends StructureFilter<NotEqualTypeOperatorFilter, Void> {

    private final MultiFilter notEqualTypeOperatorFilter

    NotEqualTypeOperatorFilter() {
        MultiFilter notEqualTypeToken1Filter = MultiFilter.matchAll(
                new RoleFilter("NOT"), new RoleFilter("IDENTICAL"), new RoleFilter("OPERATOR"), new RoleFilter("RELATIONAL"),
                new RoleFilter("EXPRESSION"), new RoleFilter("BINARY"),
                new RoleFilter("IF"), new RoleFilter("CONDITION")
        )
        MultiFilter notEqualTypeToken2Filter = MultiFilter.matchAll(
                new RoleFilter("NOT"), new RoleFilter("IDENTICAL"), new RoleFilter("OPERATOR"), new RoleFilter("RELATIONAL"),
                new RoleFilter("EXPRESSION"), new RoleFilter("BINARY")
        )
        this.notEqualTypeOperatorFilter = MultiFilter.matchAny(notEqualTypeToken1Filter, notEqualTypeToken2Filter)
    }

    @Override
    boolean evaluate(SourceNode node) {
        boolean result = this.notEqualTypeOperatorFilter.evaluate(node)
        if (result) {
            if (node.language == SourceLanguage.Php) {
                return true
            } else {
                return node.token == "!=="
            }
        }
        return false
    }
}
