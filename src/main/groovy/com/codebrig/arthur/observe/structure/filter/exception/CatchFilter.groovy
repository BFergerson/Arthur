package com.codebrig.arthur.observe.structure.filter.exception

import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.RoleFilter

/**
 * Match by catch in exception handling construct
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class CatchFilter extends StructureFilter<CatchFilter, Void> {

    private final MultiFilter catchFilter

    CatchFilter() {
        super()
        this.catchFilter = createCatchFilter()
    }

    private static createCatchFilter() {
        return MultiFilter.matchAll(
                new RoleFilter("TRY"), new RoleFilter("CATCH")
        )
    }

    @Override
    boolean evaluate(SourceNode node) {
        boolean result = this.catchFilter.evaluate(node)
        if (result) {
            if (node.language == SourceLanguage.Python) {
                if (node.roles.find{ it.toString() == "STATEMENT" }) {
                    return false
                } else {
                    return true
                }
            }
        }
        return result
    }
}
