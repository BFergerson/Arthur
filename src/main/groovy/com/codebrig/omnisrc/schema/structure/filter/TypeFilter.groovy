package com.codebrig.omnisrc.schema.structure.filter

import com.codebrig.omnisrc.SourceNode
import com.codebrig.omnisrc.schema.structure.StructureFilter

/**
 * todo: description
 *
 * @version 0.2
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class TypeFilter extends StructureFilter {

    private Set<String> acceptedTypes

    TypeFilter() {
        acceptedTypes = new HashSet<>()
    }

    TypeFilter(String... acceptTypes) {
        acceptedTypes = new HashSet<>(Arrays.asList(acceptTypes))
    }

    void acceptType(String internalType) {
        acceptedTypes.add(internalType)
    }

    @Override
    boolean evaluate(SourceNode node) {
        return acceptedTypes.contains(node?.internalType)

    }
}
