package com.codebrig.omnisrc.observe.filter.loop

import com.codebrig.omnisrc.SourceLanguage
import com.codebrig.omnisrc.SourceNode
import com.codebrig.omnisrc.SourceNodeFilter
import com.codebrig.omnisrc.observe.filter.InternalRoleFilter

/**
 * todo: this
 *
 * @version 0.3
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class ForLoopFilter extends SourceNodeFilter<ForLoopFilter, Void> {

    private static final Set<String> loopTypes = new HashSet<>()
    static {
        loopTypes.add("ForStmt") //go
        loopTypes.add("ForStatement") //java, javascript, python
    }

    @Override
    boolean evaluate(SourceNode node) {
        if (node == null) {
            return false
        }
        if (node.internalType in loopTypes) {
            if (node.language == SourceLanguage.Go) {
                def foundInit = false
                new InternalRoleFilter("Init").getFilteredNodes(node.children).each {
                    foundInit = true
                }
                return foundInit
            }
            return true
        }
        return false
    }
}
