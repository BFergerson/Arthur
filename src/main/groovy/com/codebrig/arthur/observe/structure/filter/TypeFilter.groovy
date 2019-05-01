package com.codebrig.arthur.observe.structure.filter

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter

/**
 * Match by the type
 *
 * @version 0.4
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class TypeFilter extends StructureFilter<TypeFilter, String> {

    TypeFilter(String... values) {
        accept(values)
    }

    @Override
    boolean evaluate(SourceNode node) {
        if (node != null) {
            return evaluateProperty(node.internalType)
        }
        return false
    }
}
