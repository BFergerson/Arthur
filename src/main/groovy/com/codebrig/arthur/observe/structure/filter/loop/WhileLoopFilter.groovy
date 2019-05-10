package com.codebrig.arthur.observe.structure.filter.loop

import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter
import com.codebrig.arthur.observe.structure.filter.InternalRoleFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.RoleFilter

/**
 * Match by while loop
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class WhileLoopFilter extends StructureFilter<WhileLoopFilter, Void> {

    private MultiFilter whileLoopFilter

    WhileLoopFilter() {
        super()
        this.whileLoopFilter = createWhileLoopFilter()
    }

    private static createWhileLoopFilter() {
        MultiFilter whileTokenFilter = MultiFilter.matchAll(
                new RoleFilter("WHILE"), new RoleFilter("STATEMENT"),
                new RoleFilter().reject("BLOCK", "SCOPE", "BODY")
        )
        MultiFilter forTokenFilter = MultiFilter.matchAll(
                new RoleFilter("FOR"), new RoleFilter("STATEMENT"),
                new RoleFilter().reject("BLOCK", "SCOPE", "BODY")
        )
        return MultiFilter.matchAny(whileTokenFilter, forTokenFilter)
    }

    @Override
    boolean evaluate(SourceNode node) {
        if (node == null) {
            return false
        }
        if (this.whileLoopFilter.evaluate(node)) {
            if (node.language == SourceLanguage.Go) {
                def foundInit = false
                new InternalRoleFilter("Init").getFilteredNodes(node.children).each {
                    foundInit = true
                }
                return !foundInit
            }
            return true
        }
        return false
    }
}
