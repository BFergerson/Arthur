package com.codebrig.arthur.observe.structure.filter.exception

import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.RoleFilter

/**
 * Match by try in exception handling construct
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class TryFilter extends StructureFilter<TryFilter, Void> {

    private final MultiFilter tryFilter

    TryFilter() {
        this.tryFilter = MultiFilter.matchAll(
                new RoleFilter("TRY"), new RoleFilter("STATEMENT"),
                new RoleFilter().reject("BLOCK", "SCOPE", "BODY")
        )
    }

    @Override
    boolean evaluate(SourceNode node) {
        boolean result = this.tryFilter.evaluate(node)
        if (result) {
            if (node.language == SourceLanguage.Javascript) {
                if (node.roles.find{ it.toString() == "CATCH" }) {
                    return false
                } else {
                    return true
                }
            }
            else if (node.language == SourceLanguage.Python) {
                if (node.roles.find{ it.toString() == "FINALLY" }) {
                    return false
                } else {
                    return true
                }
            }
        }
        return result
    }
}
