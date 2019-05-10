package com.codebrig.arthur.observe.structure.filter.exception

import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter
import com.codebrig.arthur.observe.structure.filter.InternalRoleFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.RoleFilter
import com.google.common.collect.Sets

/**
 * Match by finally in exception handling construct
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class FinallyFilter extends StructureFilter<FinallyFilter, Void> {

    private final MultiFilter finallyFilter

    private final Set<Integer> finallyNodeIdentities = Sets.newConcurrentHashSet()

    FinallyFilter() {
        this.finallyFilter = MultiFilter.matchAll(
                new RoleFilter("TRY"), new RoleFilter("STATEMENT"),
                new RoleFilter().reject("CATCH")
        )
    }

    @Override
    boolean evaluate(SourceNode node) {
        if (node == null) {
            return false
        }
        boolean result = this.finallyFilter.evaluate(node)
        if (result) {
            if (node.language == SourceLanguage.Java) {
                new InternalRoleFilter("finally").getFilteredNodes(node.children).each {
                    finallyNodeIdentities.add(System.identityHashCode(it.underlyingNode))
                }
            } else if (node.language == SourceLanguage.Javascript) {
                new InternalRoleFilter("finalizer").getFilteredNodes(node.children).each {
                    finallyNodeIdentities.add(System.identityHashCode(it.underlyingNode))
                }
            } else if (node.language == SourceLanguage.Python) {
                new InternalRoleFilter("finalbody").getFilteredNodes(node.children).each {
                    finallyNodeIdentities.add(System.identityHashCode(it.underlyingNode))
                }
            }
        }

        int nodeIdentity = System.identityHashCode(node.underlyingNode)
        if (finallyNodeIdentities.contains(nodeIdentity)) {
            finallyNodeIdentities.remove(nodeIdentity)
            return true
        }
        return false
    }
}
