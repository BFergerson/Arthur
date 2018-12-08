package com.codebrig.omnisrc.observe.filter.loop

import com.codebrig.omnisrc.SourceNode
import com.codebrig.omnisrc.SourceNodeFilter

/**
 * Match by do while loop
 *
 * @version 0.3
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class DoWhileLoopFilter extends SourceNodeFilter<DoWhileLoopFilter, Void> {

    private static final Set<String> loopTypes = new HashSet<>()
    static {
        loopTypes.add("DoStatement") //java
        loopTypes.add("DoWhileStatement") // javascript
    }

    @Override
    boolean evaluate(SourceNode node) {
        return node != null && node.internalType in loopTypes
    }
}
