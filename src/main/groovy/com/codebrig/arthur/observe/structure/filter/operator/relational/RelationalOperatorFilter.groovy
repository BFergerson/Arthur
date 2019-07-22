package com.codebrig.arthur.observe.structure.filter.operator.relational

import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.operator.relational.compare.IsEqualOperatorFilter
import com.codebrig.arthur.observe.structure.filter.operator.relational.compare.IsEqualTypeOperatorFilter
import com.codebrig.arthur.observe.structure.filter.operator.relational.compare.IsNotEqualOperatorFilter
import com.codebrig.arthur.observe.structure.filter.operator.relational.compare.IsNotEqualTypeOperatorFilter
import com.codebrig.arthur.observe.structure.filter.operator.relational.define.DeclareVariableOperatorFilter
import com.codebrig.arthur.observe.structure.filter.operator.relational.define.InitializeVariableOperatorFilter

/**
 * Match by relational operator
 *
 * @version 0.4
 * @since 0.4
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class RelationalOperatorFilter extends StructureFilter<RelationalOperatorFilter, Void> {

    private final MultiFilter operatorFilter
    private final MultiFilter variableDeclarationFilter

    RelationalOperatorFilter() {
        operatorFilter = MultiFilter.matchAny(
                new IsEqualOperatorFilter(),
                new IsEqualTypeOperatorFilter(),
                new IsNotEqualOperatorFilter(),
                new IsNotEqualTypeOperatorFilter(),
                //todo: inequality operators
        )
        variableDeclarationFilter = MultiFilter.matchAny(
                new DeclareVariableOperatorFilter(), new InitializeVariableOperatorFilter()
        )
    }

    @Override
    boolean evaluate(SourceNode node) {
        if (variableDeclarationFilter.evaluate(node)) {
            return true
        }
        if (node.language == SourceLanguage.Php) {
            return operatorFilter.evaluate(node)
        }

        for (def child : node.children) {
            if (node.language == SourceLanguage.Python) {
                for (def innerChild : child.children) {
                    if (operatorFilter.evaluate(innerChild)) {
                        return true
                    }
                }
            } else if (operatorFilter.evaluate(child)) {
                return true
            }
        }
        return false
    }
}
