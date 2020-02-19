package com.codebrig.arthur.observe.structure.filter.operator.relational.define

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter
import com.codebrig.arthur.observe.structure.filter.LiteralFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter

/**
 * Match by initialize variable operator
 *
 * @version 0.4
 * @since 0.4
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class InitializeVariableOperatorFilter extends StructureFilter<InitializeVariableOperatorFilter, Void> {

    private final MultiFilter filter

    InitializeVariableOperatorFilter() {
        filter = MultiFilter.matchAll(
                new DeclareVariableOperatorFilter()
        )
    }

    @Override
    boolean evaluate(SourceNode node) {
        //todo: new objects, method calls
        return filter.evaluate(node) && new LiteralFilter().getFilteredNodesIncludingCurrent(node) != null
    }
}
