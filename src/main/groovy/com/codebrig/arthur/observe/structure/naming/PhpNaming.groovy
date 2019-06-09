package com.codebrig.arthur.observe.structure.naming

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureNaming
import com.codebrig.arthur.observe.structure.filter.TypeFilter
import com.codebrig.arthur.util.Util

/**
 * Used to get the names of PHP AST nodes
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class PhpNaming implements StructureNaming {

    @Override
    boolean isNamedNodeType(String internalType) {
        switch (Objects.requireNonNull(internalType)) {
            case "StmtFunction":
            case "Stmt_Function":
            case "Expr_Closure":
                return true
            default:
                return false
        }
    }

    @Override
    String getNodeName(SourceNode node) {
        switch (Objects.requireNonNull(node).internalType) {
            case "StmtFunction":
            case "Stmt_Function":
            case "Expr_Closure":
                return getFunctionName(node)
            default:
                return null
        }
    }

    static String getFunctionName(SourceNode node) {
        def functionName = ""
        def hasFunctionName = false
        node.children.each {
            switch (it.internalType) {
                case "Name":
                    functionName += it.token + "("
                    hasFunctionName = true
                    break
                case "Param":
                    new TypeFilter("Name").getFilteredNodes(it.children).each {
                        functionName += it.token + ","
                    }
                    break
            }
        }
        functionName = Util.trimTrailingComma(functionName)
        if (hasFunctionName) {
            functionName += ")"
        }
        return functionName
    }
}
