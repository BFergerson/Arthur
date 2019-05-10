package com.codebrig.arthur.observe.structure.filter.operator.relational

import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.RoleFilter

/**
 * Match by equal type operator
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class EqualTypeOperatorFilter extends StructureFilter<EqualTypeOperatorFilter, Void> {

    private final MultiFilter equalTypeOperatorFilter

    EqualTypeOperatorFilter() {
        super()
        this.equalTypeOperatorFilter = createEqualTypeOperatorFilter()
    }

    private static createEqualTypeOperatorFilter() {
        MultiFilter equalTypeToken1Filter = MultiFilter.matchAll(
                new RoleFilter("IDENTICAL"), new RoleFilter("OPERATOR"), new RoleFilter("RELATIONAL"),
                new RoleFilter("EXPRESSION"), new RoleFilter("BINARY")
        )
        MultiFilter equalTypeToken2Filter = MultiFilter.matchAll(equalTypeToken1Filter,
                new RoleFilter("IF"), new RoleFilter("CONDITION")
        )
        return MultiFilter.matchAny(equalTypeToken1Filter, equalTypeToken2Filter)
    }

    @Override
    boolean evaluate(SourceNode node) {
        boolean result = this.equalTypeOperatorFilter.evaluate(node)
        if (result) {
            if (node.language == SourceLanguage.Php) {
                return true
            } else {
                return node.token == "==="
            }
        }
        return false
    }
}
