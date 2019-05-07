package com.codebrig.arthur.observe.structure.filter.conditional

import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter
import com.codebrig.arthur.observe.structure.filter.InternalRoleFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.RoleFilter
import com.codebrig.arthur.observe.structure.filter.TypeFilter
import com.google.common.collect.Sets

/**
 * Match by else conditional
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class ElseConditionalFilter extends StructureFilter<ElseConditionalFilter, Void> {

    private final MultiFilter elseConditionalFilter

    private final Set<Integer> elseNodeIdentities = Sets.newConcurrentHashSet()

    ElseConditionalFilter() {
        MultiFilter elseStatementFilter = MultiFilter.matchAll(
                new RoleFilter("IF"), new RoleFilter("STATEMENT")
        )
        MultiFilter elseExpressionFilter = MultiFilter.matchAll(
                new RoleFilter("IF"), new RoleFilter("EXPRESSION")
        )
        this.elseConditionalFilter = MultiFilter.matchAny(elseStatementFilter, elseExpressionFilter)
    }

    @Override
    boolean evaluate(SourceNode node) {
        if (node == null) {
            return false
        }
        if (this.elseConditionalFilter.evaluate(node)) {
            if (node.language == SourceLanguage.Python) {
                MultiFilter.matchAll(
                        new InternalRoleFilter("orelse"),
                        new TypeFilter("If.orelse")
                ).getFilteredNodes(node.children).each {
                    MultiFilter.matchAll(
                            new InternalRoleFilter("else_stmts"),
                            new TypeFilter().reject("If")
                    ).getFilteredNodes(it.children).each {
                        elseNodeIdentities.add(System.identityHashCode(it.underlyingNode))
                    }
                }
            } else if (node.language == SourceLanguage.Java) {
                MultiFilter.matchAll(
                        new InternalRoleFilter("elseStatement"),
                        new TypeFilter().reject("IfStatement")
                ).getFilteredNodes(node.children).each {
                    elseNodeIdentities.add(System.identityHashCode(it.underlyingNode))
                }
            } else if (node.language == SourceLanguage.Javascript) {
                MultiFilter.matchAll(
                        new InternalRoleFilter("alternate"),
                        new TypeFilter().reject("IfStatement")
                ).getFilteredNodes(node.children).each {
                    elseNodeIdentities.add(System.identityHashCode(it.underlyingNode))
                }
            } else if (node.language == SourceLanguage.Go) {
                MultiFilter.matchAll(
                        new InternalRoleFilter("Else"),
                        new TypeFilter().reject("IfStmt")
                ).getFilteredNodes(node.children).each {
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
