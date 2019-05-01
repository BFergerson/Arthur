package com.codebrig.arthur.observe.structure.filter.loop

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter

/**
 * Match by for each loop
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class ForEachLoopFilter extends StructureFilter<ForEachLoopFilter, Void> {

    private static final Set<String> loopTypes = new HashSet<>()
    static {
        loopTypes.add("RangeStmt") //go
        loopTypes.add("EnhancedForStatement") //java
        loopTypes.add("ForInStatement")// javascript
    }

    @Override
    boolean evaluate(SourceNode node) {
        return node != null && node.internalType in loopTypes
    }
}
