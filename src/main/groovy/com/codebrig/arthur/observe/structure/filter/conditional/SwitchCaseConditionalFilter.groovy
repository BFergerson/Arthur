package com.codebrig.arthur.observe.structure.filter.conditional

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter

/**
 * Match by case in switch conditional
 *
 * @version 0.3.2
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class SwitchCaseConditionalFilter extends StructureFilter<SwitchCaseConditionalFilter, Void> {

    private static final Set<String> conditionalTypes = new HashSet<>()
    static {
        conditionalTypes.add("CaseClause") //go
        conditionalTypes.add("SwitchCase") //java, javascript
    }

    @Override
    boolean evaluate(SourceNode node) {
        return node != null && node.internalType in conditionalTypes
    }
}
