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
class WhitelistRoleFilter extends SourceNodeFilter {

    private Set<String> acceptedRoles

    WhitelistRoleFilter() {
        this.acceptedRoles = new HashSet<>()
    }

    WhitelistRoleFilter(String... acceptRoles) {
        this.acceptedRoles = new HashSet<>(Arrays.asList(acceptRoles))
    }

    void acceptRole(String role) {
        acceptedRoles.add(Objects.requireNonNull(role).toUpperCase())
    }

    @Override
    boolean evaluate(SourceNode node) {
        if (node != null) {
            def roleList = new ArrayList<String>()
            boolean foundAccept = false
            node.roles.each {
                roleList.add(it.name())
                if (acceptedRoles.contains(it.name())) {
                    foundAccept = true
                }
            }
            if (foundAccept) {
                return true
            }

            if (roleList.size() > 1) {
                roleList.sort(String.CASE_INSENSITIVE_ORDER)
                def sb = new StringBuilder()
                for (int i = 0; i < roleList.size(); i++) {
                    sb.append(roleList.get(i))
                    if ((i + 1) < roleList.size()) {
                        sb.append("_")
                    }
                }
                return acceptedRoles.contains(sb.toString())
            }
        }
        return false
    }
}
