package com.codebrig.omnisrc.observe.filter

import com.codebrig.omnisrc.SourceNode
import com.codebrig.omnisrc.SourceNodeFilter

/**
 * Match by the semantic role
 *
 * @version 0.3.2
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class RoleFilter extends SourceNodeFilter<RoleFilter, String> {

    RoleFilter(String... values) {
        accept(values)
    }

    @Override
    RoleFilter accept(String... values) {
        super.accept(values.collect { it.toUpperCase() }.toArray(new String[0]))
        return this
    }

    @Override
    RoleFilter reject(String... values) {
        super.reject(values.collect { it.toUpperCase() }.toArray(new String[0]))
        return this
    }

    @Override
    boolean evaluate(SourceNode node) {
        if (node != null) {
            def roleList = new ArrayList<String>()
            boolean foundReject = false
            boolean foundAccept = false
            node.roles.collect { it.name().toUpperCase() }.each {
                roleList.add(it)
                if (acceptSet.contains(it)) {
                    foundAccept = true
                } else if (rejectSet.contains(it)) {
                    foundReject = true
                }
            }
            if (foundReject) {
                return false
            }

            if (roleList.size() > 1) {
                roleList.sort(String.CASE_INSENSITIVE_ORDER)
                def sb = new StringBuilder()
                for (int i = 0; i < roleList.size(); i++) {
                    sb.append(roleList.get(i).toUpperCase())
                    if ((i + 1) < roleList.size()) {
                        sb.append("_")
                    }
                }
                return ((foundAccept || acceptSet.contains(sb.toString())) && !rejectSet.contains(sb.toString())) ||
                        (acceptSet.isEmpty() && !rejectSet.contains(sb.toString()))
            }
            return foundAccept || (acceptSet.isEmpty() && !foundReject)
        }
        return false
    }
}
