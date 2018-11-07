package com.codebrig.omnisrc.structure.filter

import com.codebrig.omnisrc.SourceNode
import com.codebrig.omnisrc.structure.StructureFilter

/**
 * todo: description
 *
 * @version 0.2
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class MultiFilter implements StructureFilter {

    private final List<StructureFilter> filters

    MultiFilter() {
        this.filters = new ArrayList<>()
    }

    MultiFilter(StructureFilter... filters) {
        this.filters = Arrays.asList(filters)
    }

    void acceptFilter(StructureFilter filter) {
        filters.add(Objects.requireNonNull(filter))
    }

    @Override
    boolean evaluate(SourceNode object) {
        return filters.any { it.evaluate(object) }
    }
}
