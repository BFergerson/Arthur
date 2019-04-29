package com.codebrig.omnisrc.observe.filter.operator.logical

import com.codebrig.omnisrc.SourceNode
import com.codebrig.omnisrc.SourceNodeFilter

/**
 * Match by logical or operator
 *
 * @version 0.3.1
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class OrOperatorFilter extends SourceNodeFilter<OrOperatorFilter, Void> {

    private static final Set<String> operatorTypes = new HashSet<>()
    static {
        operatorTypes.add("Or") //python
        operatorTypes.add("Operator") //go, java, javascript
    }

    @Override
    boolean evaluate(SourceNode node) {
        return node != null && node.internalType in operatorTypes && (node.token == "||" || node.token == "or")
    }
}
