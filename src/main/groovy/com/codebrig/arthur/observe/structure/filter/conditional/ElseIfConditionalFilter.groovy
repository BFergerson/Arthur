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

    private final Set<Integer> elseIfNodeIdentities = Sets.newConcurrentHashSet()

    ElseIfConditionalFilter() {
        filter = MultiFilter.matchAll(
                new RoleFilter("IF"),
                new RoleFilter("STATEMENT", "EXPRESSION")
        )
    }

    @Override
    boolean evaluate(SourceNode node) {
        boolean result = filter.evaluate(node)
        if (result) {
            MultiFilter.matchAll(
                    new InternalRoleFilter("elseStatement", "alternate", "Else", "Prop_ElseClause", "else", "elseifs"),
                    new TypeFilter("If", "IfStatement", "IfStmt", "CPPASTIfStatement", "if", "Stmt_ElseIf")
            ).getFilteredNodes(node.children).each {
                elseIfNodeIdentities.add(System.identityHashCode(it.underlyingNode))
            }
            MultiFilter.matchAll(
                    new InternalRoleFilter("orelse"),
                    new TypeFilter("If.orelse")
            ).getFilteredNodes(node.children).each {
                MultiFilter.matchAll(
                        new InternalRoleFilter("else_stmts"),
                        new TypeFilter("If")
                ).getFilteredNodes(it.children).each {
                    elseIfNodeIdentities.add(System.identityHashCode(it.underlyingNode))
                }
            }
            new TypeFilter("ElseClause").getFilteredNodes(node.children).each {
                new TypeFilter("IfStatement").getFilteredNodes(it.children).each {
                    elseIfNodeIdentities.add(System.identityHashCode(it.underlyingNode))
                }
            }
        }

        int nodeIdentity = System.identityHashCode(node.underlyingNode)
        if (elseIfNodeIdentities.contains(nodeIdentity)) {
            elseIfNodeIdentities.remove(nodeIdentity)
            return true
        }
        return false
    }
}
