package com.codebrig.arthur.observe.structure.filter.conditional

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter

/**
 * Match by if conditional
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class IfConditionalFilter extends StructureFilter<IfConditionalFilter, Void> {

    private static final Set<String> conditionalTypes = new HashSet<>()
    static {
        conditionalTypes.add("If") //python
        conditionalTypes.add("IfStmt") //go
        conditionalTypes.add("IfStatement") //java, javascript
    }

    @Override
    boolean evaluate(SourceNode node) {
        return node != null && node.internalType in conditionalTypes
    }
}
