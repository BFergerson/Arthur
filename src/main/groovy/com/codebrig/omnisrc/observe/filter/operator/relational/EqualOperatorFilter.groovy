package com.codebrig.omnisrc.observe.filter.operator.relational

import com.codebrig.omnisrc.SourceNode
import com.codebrig.omnisrc.SourceNodeFilter

/**
 * Match by equal operator
 *
 * @version 0.3.1
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class EqualOperatorFilter extends SourceNodeFilter<EqualOperatorFilter, Void> {

    private static final Set<String> operatorTypes = new HashSet<>()
    static {
        operatorTypes.add("Eq") //python
        operatorTypes.add("Operator") //go, java, javascript
    }

    @Override
    boolean evaluate(SourceNode node) {
        return node != null && node.internalType in operatorTypes && node.token == "=="
    }
}
