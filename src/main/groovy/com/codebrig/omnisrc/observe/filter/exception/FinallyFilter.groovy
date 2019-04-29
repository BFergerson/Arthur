package com.codebrig.omnisrc.observe.filter.exception

import com.codebrig.omnisrc.SourceLanguage
import com.codebrig.omnisrc.SourceNode
import com.codebrig.omnisrc.SourceNodeFilter
import com.codebrig.omnisrc.observe.filter.InternalRoleFilter
import com.google.common.collect.Sets

/**
 * Match by finally in exception handling construct
 *
 * @version 0.3.2
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class FinallyFilter extends SourceNodeFilter<FinallyFilter, Void> {

    private static final Set<String> exceptionTypes = new HashSet<>()
    static {
        exceptionTypes.add("TryFinally") //python
        exceptionTypes.add("TryStatement") //java, javascript
    }
    private final Set<Integer> finallyNodeIdentities = Sets.newConcurrentHashSet()

    @Override
    boolean evaluate(SourceNode node) {
        if (node == null) {
            return false
        }
        if (node.internalType in exceptionTypes) {
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
