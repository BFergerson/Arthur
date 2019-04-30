package com.codebrig.omnisrc.observe.structure.filter.operator.relational

import com.codebrig.omnisrc.SourceLanguage
import com.codebrig.omnisrc.SourceNode
import com.codebrig.omnisrc.SourceNodeFilter

/**
 * Match by equal operator
 *
 * @version 0.3.2
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class EqualOperatorFilter extends SourceNodeFilter<EqualOperatorFilter, Void> {

    private static final Set<String> operatorTypes = new HashSet<>()
    static {
        operatorTypes.add("Eq") //python
        operatorTypes.add("Operator") //go, java, javascript
        operatorTypes.add("Expr_BinaryOp_Equal") //php
    }

    @Override
    boolean evaluate(SourceNode node) {
        if (node != null && node.internalType in operatorTypes) {
            if (node.language == SourceLanguage.Php) {
                return true
            } else {
                return node.token == "=="
            }
        }
        return false
    }
}
