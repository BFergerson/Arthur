package com.codebrig.omnisrc.observe.filter.operator.misc

import com.codebrig.omnisrc.SourceNode
import com.codebrig.omnisrc.SourceNodeFilter

/**
 * todo: this
 *
 * @version 0.3
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class TernaryOperatorFilter extends SourceNodeFilter<TernaryOperatorFilter, Void> {

    private static final Set<String> operatorTypes = new HashSet<>()
    static {
        operatorTypes.add("ConditionalExpression") //java, javascript
    }

    @Override
    boolean evaluate(SourceNode node) {
        return node != null && node.internalType in operatorTypes
    }
}
