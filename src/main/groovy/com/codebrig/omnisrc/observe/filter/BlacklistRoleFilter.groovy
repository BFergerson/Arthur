package com.codebrig.omnisrc.observe.filter

import com.codebrig.omnisrc.SourceNode
import com.codebrig.omnisrc.SourceNodeFilter

/**
 * todo: description
 *
 * @version 0.2
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class BlacklistRoleFilter extends SourceNodeFilter {

    private Set<String> rejectedRoles

    BlacklistRoleFilter() {
        this.rejectedRoles = new HashSet<>()
    }

    BlacklistRoleFilter(String... rejectRoles) {
        this.rejectedRoles = new HashSet<>(Arrays.asList(rejectRoles))
    }

    void rejectRole(String role) {
        rejectedRoles.add(Objects.requireNonNull(role).toUpperCase())
    }

    @Override
    boolean evaluate(SourceNode node) {
        if (node != null) {
            def roleList = new ArrayList<String>()
            boolean foundReject = false
            node.roles.each {
                roleList.add(it.name())
                if (rejectedRoles.contains(it.name())) {
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
                    sb.append(roleList.get(i))
                    if ((i + 1) < roleList.size()) {
                        sb.append("_")
                    }
                }
                return !rejectedRoles.contains(sb.toString())
            }
        }
        return true
    }
}
