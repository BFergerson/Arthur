package com.codebrig.omnisrc.structure.filter

import gopkg.in.bblfsh.sdk.v1.uast.generated.Node
import org.apache.commons.collections4.iterators.FilterIterator
import org.bblfsh.client.BblfshClient

/**
 * todo: description
 *
 * @version 0.2
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class RoleFilter implements StructureFilter {

    private Set<String> acceptedRoles

    RoleFilter() {
        acceptedRoles = new HashSet<>()
    }

    RoleFilter(String... acceptRoles) {
        acceptedRoles = new HashSet<>(Arrays.asList(acceptRoles))
    }

    void acceptRole(String role) {
        acceptedRoles.add(role)
    }

    @Override
    Iterator<Node> getFilteredNodes(Node uastNodes) {
        return new FilterIterator(asJavaIterator(BblfshClient.iterator(uastNodes, BblfshClient.PreOrder())), this)
    }

    @Override
    boolean evaluate(Node node) {
        if (node != null) {
            def roleList = new ArrayList<String>()
            boolean foundRole = asJavaIterator(node.roles()).any {
                roleList.add(it.name())
                return acceptedRoles.contains(it.name())
            }
            if (!foundRole && roleList.size() > 1) {
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
            return foundRole
        }
        return false
    }
}
