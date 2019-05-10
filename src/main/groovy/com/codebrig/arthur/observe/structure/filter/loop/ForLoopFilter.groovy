package com.codebrig.arthur.observe.structure.filter.loop

import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter
import com.codebrig.arthur.observe.structure.filter.InternalRoleFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.RoleFilter

/**
 * Match by for loop
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class ForLoopFilter extends StructureFilter<ForLoopFilter, Void> {

    private final MultiFilter forLoopFilter

    ForLoopFilter() {
        super()
        this.forLoopFilter = createForLoopFilter()
    }

    private static MultiFilter createForLoopFilter() {
        return MultiFilter.matchAll(new RoleFilter("FOR"), new RoleFilter("STATEMENT"))
    }

    @Override
    boolean evaluate(SourceNode node) {
        if (node == null) {
            return false
        }
        if (this.forLoopFilter.evaluate(node)) {
            switch (node.language) {
                case SourceLanguage.Go:
                case SourceLanguage.Php:
                case SourceLanguage.Javascript:
                    def foundInit = false
                    new InternalRoleFilter("Init").getFilteredNodes(node.children).each {
                        foundInit = true
                    }
                    return foundInit
                case SourceLanguage.Java:
                    def foundInit = false
                    new InternalRoleFilter("initializers").getFilteredNodes(node.children).each {
                        foundInit = true
                    }
                    return foundInit
            }
            return true
        }
        return false
    }
}
