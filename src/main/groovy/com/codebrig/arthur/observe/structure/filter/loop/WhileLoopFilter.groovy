package com.codebrig.arthur.observe.structure.filter.loop

import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter
import com.codebrig.arthur.observe.structure.filter.InternalRoleFilter

/**
 * Match by while loop
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class WhileLoopFilter extends StructureFilter<WhileLoopFilter, Void> {

    private static final Set<String> loopTypes = new HashSet<>()
    static {
        loopTypes.add("While") //python
        loopTypes.add("ForStmt") //go
        loopTypes.add("WhileStatement") //java, javascript
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
                return !foundInit
            }
            return true
        }
        return false
    }
}
