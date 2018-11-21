package com.codebrig.omnisrc.observe.filter

import com.codebrig.omnisrc.SourceNode
import com.codebrig.omnisrc.SourceNodeFilter

/**
 * Match by the internal role
 *
 * @version 0.3
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class InternalRoleFilter extends SourceNodeFilter {

    private Set<String> acceptedRoles
    private Set<String> rejectedRoles

    InternalRoleFilter() {
        acceptedRoles = new HashSet<>()
        rejectedRoles = new HashSet<>()
    }

    InternalRoleFilter(String... acceptRoles) {
        acceptedRoles = new HashSet<>(Arrays.asList(acceptRoles))
        rejectedRoles = new HashSet<>()
    }

    void acceptRole(String role) {
        acceptedRoles.add(Objects.requireNonNull(role).toUpperCase())
    }

    void rejectRole(String role) {
        rejectedRoles.add(Objects.requireNonNull(role).toUpperCase())
    }

    @Override
    boolean evaluate(SourceNode node) {
        if (node != null) {
            def internalRole = node.properties.get("internalRole")
            return acceptedRoles.contains(internalRole) && !rejectedRoles.contains(internalRole)
        }
        return false
    }
}
