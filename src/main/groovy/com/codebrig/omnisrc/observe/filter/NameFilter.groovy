package com.codebrig.omnisrc.observe.filter

import com.codebrig.omnisrc.SourceNodeFilter
import com.codebrig.omnisrc.SourceNode

/**
 * todo: description
 *
 * @version 0.2
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class NameFilter extends SourceNodeFilter {

    private final String name

    NameFilter(String name) {
        this.name = name
    }

    @Override
    boolean evaluate(SourceNode node) {
        if (node != null) {
            def childNameFilter = new InternalRoleFilter("name").getFilteredNodes(node.children)
            return new TokenFilter(name).getFilteredNodes(childNameFilter).hasNext()
        }
        return false
    }
}
