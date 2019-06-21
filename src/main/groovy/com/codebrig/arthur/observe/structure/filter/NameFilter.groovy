package com.codebrig.arthur.observe.structure.filter

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter

/**
 * Match by the fully qualified name
 *
 * @version 0.4
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class NameFilter extends StructureFilter<NameFilter, String> {

    NameFilter(String... values) {
        accept(values)
    }

    @Override
    boolean evaluate(SourceNode node) {
        return node != null && node.hasName() && evaluateProperty(node.name)
    }
}
