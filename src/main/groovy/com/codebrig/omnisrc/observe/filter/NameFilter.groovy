package com.codebrig.omnisrc.observe.filter

import com.codebrig.omnisrc.SourceLanguage
import com.codebrig.omnisrc.SourceNode
import com.codebrig.omnisrc.SourceNodeFilter

/**
 * Match by the name
 *
 * @version 0.3
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class NameFilter extends SourceNodeFilter<NameFilter, String> {

    NameFilter(String... values) {
        accept(values)
    }

    @Override
    boolean evaluate(SourceNode node) {
        if (node != null) {
            if (node.language == SourceLanguage.Python) {
                return evaluateProperty(node.token)
            }

            def childNameFilter
            if (node.language == SourceLanguage.Javascript) {
                childNameFilter = new InternalRoleFilter("id").getFilteredNodes(node.children)
            } else {
                childNameFilter = new InternalRoleFilter("name").getFilteredNodes(node.children)
            }
            return new TokenFilter()
                    .accept(acceptSet.toArray(new String[0]))
                    .reject(rejectSet.toArray(new String[0]))
                    .getFilteredNodes(childNameFilter).hasNext()
        }
        return false
    }
}
