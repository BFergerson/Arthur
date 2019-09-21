package com.codebrig.arthur.observe.structure.filter.operator.relational.define

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.RoleFilter
import com.codebrig.arthur.observe.structure.filter.TypeFilter

/**
 * Match by declare variable operator
 *
 * @version 0.4
 * @since 0.4
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 * @author <a href="mailto:valpecaoco@gmail.com">Val Pecaoco</a>
 */
class DeclareVariableOperatorFilter extends StructureFilter<DeclareVariableOperatorFilter, Void> {

    private final MultiFilter filter

    DeclareVariableOperatorFilter() {
        filter = MultiFilter.matchAny(
                MultiFilter.matchAll(
                        new RoleFilter("DECLARATION"), new RoleFilter("VARIABLE"),
                        new RoleFilter().reject("NAME"),
                        new TypeFilter().reject("VariableDeclarationFragment", "VariableDeclarator")
                ),
                MultiFilter.matchAll(
                        new RoleFilter("DECLARATION"), new RoleFilter("ASSIGNMENT"),
                        new TypeFilter().reject("CPPASTEqualsInitializer")
                ),
                MultiFilter.matchAll(
                        new RoleFilter("EXPRESSION"), new RoleFilter("ASSIGNMENT"),
                        new TypeFilter().reject("CPPASTEqualsInitializer")
                ),
                MultiFilter.matchAll(
                        new RoleFilter("DECLARATION"), new RoleFilter("STATEMENT"),
                        new TypeFilter("CPPASTSimpleDeclaration")
                )
        )
    }

    @Override
    boolean evaluate(SourceNode node) {
        return filter.evaluate(node)
    }
}
