package com.codebrig.omnisrc.observe.structure.filter.operator.relational

import com.codebrig.omnisrc.SourceLanguage
import com.codebrig.omnisrc.SourceNode
import com.codebrig.omnisrc.SourceNodeFilter

/**
 * Match by equal type operator
 *
 * @version 0.3.2
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class EqualTypeOperatorFilter extends SourceNodeFilter<EqualTypeOperatorFilter, Void> {

    private static final Set<String> operatorTypes = new HashSet<>()
    static {
        operatorTypes.add("Operator") //javascript
        operatorTypes.add("Expr_BinaryOp_Identical") //php
    }

    @Override
    boolean evaluate(SourceNode node) {
        if (node != null && node.internalType in operatorTypes) {
            if (node.language == SourceLanguage.Php) {
                return true
            } else {
                return node.token == "==="
            }
        }
        return false
    }
}
