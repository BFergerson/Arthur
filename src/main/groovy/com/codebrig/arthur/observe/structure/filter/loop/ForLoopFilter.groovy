package com.codebrig.arthur.observe.structure.filter.loop

import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter
import com.codebrig.arthur.observe.structure.filter.InternalRoleFilter

/**
 * Match by for loop
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class ForLoopFilter extends StructureFilter<ForLoopFilter, Void> {

    public static final Set<String> LOOP_TYPES = new HashSet<>()
    static {
        LOOP_TYPES.add("for") //ruby
        LOOP_TYPES.add("For") //python
        LOOP_TYPES.add("ForStmt") //go
        LOOP_TYPES.add("ForStatement") //java, javascript
        LOOP_TYPES.add("Stmt_For") //php
    }

    @Override
    boolean evaluate(SourceNode node) {
        if (node == null) {
            return false
        }
        if (node.internalType in LOOP_TYPES) {
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
