package com.codebrig.omnisrc.structure.filter

import gopkg.in.bblfsh.sdk.v1.uast.generated.Node

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
    boolean evaluate(Node object) {
        return filters.any { it.evaluate(object) }
    }
}
