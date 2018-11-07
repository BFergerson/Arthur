package com.codebrig.omnisrc.structure.filter

import gopkg.in.bblfsh.sdk.v1.uast.generated.Node

/**
 * todo: description
 *
 * @version 0.2
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class RoleFilter implements StructureFilter {

    private Set<String> acceptedRoles
    private Set<String> rejectedRoles

    RoleFilter() {
        acceptedRoles = new HashSet<>()
        rejectedRoles = new HashSet<>()
    }

    RoleFilter(String... acceptRoles) {
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
    boolean evaluate(Node node) {
        if (node != null) {
            def roleList = new ArrayList<String>()
            boolean foundReject = false
            boolean foundAccept = false
            asJavaIterator(node.roles()).each {
                roleList.add(it.name())
                if (acceptedRoles.contains(it.name())) {
                    foundAccept = true
                } else if (rejectedRoles.contains(it.name())) {
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
                return (foundAccept || acceptedRoles.contains(sb.toString())) && !rejectedRoles.contains(sb.toString())
            }
            return foundAccept
        }
        return false
    }
}
