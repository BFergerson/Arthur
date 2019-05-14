package com.codebrig.arthur.observe.structure.filter.conditional

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter
import com.codebrig.arthur.observe.structure.filter.InternalRoleFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.RoleFilter
import com.codebrig.arthur.observe.structure.filter.TypeFilter
import com.google.common.collect.Sets

/**
 * Match by else if conditional
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class ElseIfConditionalFilter extends StructureFilter<ElseIfConditionalFilter, Void> {

    private final MultiFilter filter

    private final Set<Integer> elseNodeIdentities = Sets.newConcurrentHashSet()

    ElseIfConditionalFilter() {
        this.filter = MultiFilter.matchAll(new RoleFilter("IF"), new RoleFilter("STATEMENT", "EXPRESSION"))
    }

    @Override
    boolean evaluate(SourceNode node) {
        boolean result = this.filter.evaluate(node)
        if (result) {
            MultiFilter.matchAll(
                    new InternalRoleFilter("elseStatement", "alternate", "Else"),
                    new TypeFilter("If", "IfStatement", "IfStmt")
            ).getFilteredNodes(node.children).each {
                elseNodeIdentities.add(System.identityHashCode(it.underlyingNode))
            }
            MultiFilter.matchAll(
                    new InternalRoleFilter("orelse"),
                    new TypeFilter("If.orelse")
            ).getFilteredNodes(node.children).each {
                MultiFilter.matchAll(
                        new InternalRoleFilter("else_stmts"),
                        new TypeFilter("If")
                ).getFilteredNodes(it.children).each {
                    elseNodeIdentities.add(System.identityHashCode(it.underlyingNode))
                }
            }
        }

        int nodeIdentity = System.identityHashCode(node.underlyingNode)
        if (elseNodeIdentities.contains(nodeIdentity)) {
            elseNodeIdentities.remove(nodeIdentity)
            return true
        }
        return false
    }
}
