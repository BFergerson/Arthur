package com.codebrig.arthur.observe.structure.filter

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter

/**
 * Match by function declaration
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class FunctionFilter extends StructureFilter<FunctionFilter, Void> {

    private final MultiFilter filter

    FunctionFilter() {
        filter = MultiFilter.matchAny(
                MultiFilter.matchAll(
                        new RoleFilter("DECLARATION"), new RoleFilter("FUNCTION"),
                        new TypeFilter("FunctionDeclaration", "FunctionExpression",
                                "ArrowFunctionExpression", "ObjectMethod", "FuncDecl", "FunctionDef",
                                "MethodDeclaration", "Stmt_Function", "Expr_Closure", "def", "defs"),
                        new RoleFilter().reject("ARGUMENT", "RETURN", "INCOMPLETE", "BODY")
                ),
                MultiFilter.matchAll(
                        new TypeFilter("NewExpression"),
                        new InternalRoleFilter("init")
                ),
                MultiFilter.matchAll(
                        new TypeFilter("defs"),
                        new InternalRoleFilter("body")
                )
        )
    }

    @Override
    boolean evaluate(SourceNode node) {
        return filter.evaluate(node)
    }
}
