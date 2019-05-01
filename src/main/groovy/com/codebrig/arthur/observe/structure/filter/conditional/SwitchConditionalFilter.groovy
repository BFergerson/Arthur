package com.codebrig.arthur.observe.structure.filter.conditional

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.SourceNodeFilter

/**
 * Match by switch conditional
 *
 * @version 0.3.2
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class SwitchConditionalFilter extends SourceNodeFilter<SwitchConditionalFilter, Void> {

    private static final Set<String> conditionalTypes = new HashSet<>()
    static {
        conditionalTypes.add("SwitchStmt") //go
        conditionalTypes.add("SwitchStatement") //java, javascript
    }

    @Override
    boolean evaluate(SourceNode node) {
        return node != null && node.internalType in conditionalTypes
    }
}
