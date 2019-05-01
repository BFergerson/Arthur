package com.codebrig.arthur.observe.structure.filter.operator.relational

import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter

/**
 * Match by equal type operator
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class EqualTypeOperatorFilter extends StructureFilter<EqualTypeOperatorFilter, Void> {

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
