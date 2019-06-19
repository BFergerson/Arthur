package com.codebrig.arthur.observe.structure.filter

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter
import com.google.common.collect.Sets

/**
 * Match by literal parameter name
 *
 * @version 0.4
 * @since 0.4
 * @author <a href="mailto:valpecaoco@gmail.com">Val Pecaoco</a>
 */
class LiteralFilter extends StructureFilter<LiteralFilter, String> {

    private final MultiFilter filter

    private final Set<Integer> literalNodeIdentities = Sets.newConcurrentHashSet()

    LiteralFilter(String... values) {
        accept(values)
        filter = MultiFilter.matchAll(
                new RoleFilter("DECLARATION"), new RoleFilter("VARIABLE"),
                new TypeFilter("VariableDeclarationFragment")
        )
    }

    @Override
    boolean evaluate(SourceNode node) {
        boolean result = filter.evaluate(node)
        if (result) {
            MultiFilter.matchAll(
                    new TypeFilter("SimpleName"),
                    new InternalRoleFilter("name")
            ).getFilteredNodes(node.children).each {
                if (evaluateProperty(it.token)) {
                    literalNodeIdentities.add(System.identityHashCode(node))
                }
            }
        }

        int nodeIdentity = System.identityHashCode(node)
        if (literalNodeIdentities.contains(nodeIdentity)) {
            literalNodeIdentities.remove(nodeIdentity)
            return true
        }
        return false
    }
}
