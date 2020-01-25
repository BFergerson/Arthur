package com.codebrig.arthur.observe.structure.filter

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter

/**
 * Match by the internal role
 *
 * @version 0.4
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class InternalRoleFilter extends StructureFilter<InternalRoleFilter, String> {

    InternalRoleFilter(String... values) {
        accept(values)
    }

    @Override
    InternalRoleFilter accept(String... values) {
        super.accept(values.collect { it.toUpperCase() }.toArray(new String[0]))
        return this
    }

    @Override
    InternalRoleFilter reject(String... values) {
        super.reject(values.collect { it.toUpperCase() }.toArray(new String[0]))
        return this
    }

    @Override
    boolean evaluate(SourceNode node) {
        if (node != null) {
            return evaluateProperty(node.properties.get("internalRole").toUpperCase())
        }
        return false
    }
}
