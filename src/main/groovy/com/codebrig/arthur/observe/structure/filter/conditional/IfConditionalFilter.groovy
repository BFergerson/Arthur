package com.codebrig.arthur.observe.structure.filter.conditional

import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.RoleFilter

/**
 * Match by if conditional
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class IfConditionalFilter extends StructureFilter<IfConditionalFilter, Void> {

    private final MultiFilter filter

    IfConditionalFilter() {
        filter = MultiFilter.matchAll(
                new RoleFilter("IF"), new RoleFilter("STATEMENT", "EXPRESSION"),
                new RoleFilter().reject("BLOCK", "SCOPE", "THEN", "BODY", "IDENTIFIER", "CONDITION", "ELSE")
        )
    }

    @Override
    boolean evaluate(SourceNode node) {
        if (filter.evaluate(node)) {
            if (node.language == SourceLanguage.CSharp) {
                return node.parentSourceNode?.internalType != "ElseClause"
            } else if (node.language == SourceLanguage.Python) {
                return node.parentSourceNode?.internalType != "If.orelse"
            }

            return true
        }
        return false
    }
}
