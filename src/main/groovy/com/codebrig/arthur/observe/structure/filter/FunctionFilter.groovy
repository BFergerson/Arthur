package com.codebrig.arthur.observe.structure.filter

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter

/**
 * Match by function declaration
 *
 * @version 0.3.2
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class FunctionFilter extends StructureFilter<FunctionFilter, Void> {

    private static final Set<String> functionTypes = new HashSet<>()
    static {
        functionTypes.add("def") //ruby
        functionTypes.add("FuncDecl") //go
        functionTypes.add("MethodDeclaration") //java
        functionTypes.add("FunctionDeclaration") //js
        functionTypes.add("Stmt_Function") //php
        functionTypes.add("FunctionDef") //python
    }

    @Override
    boolean evaluate(SourceNode node) {
        return node != null && node.internalType in functionTypes
    }
}
