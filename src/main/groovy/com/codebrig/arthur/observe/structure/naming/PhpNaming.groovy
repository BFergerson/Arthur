package com.codebrig.arthur.observe.structure.naming

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureNaming
import com.codebrig.arthur.observe.structure.filter.InternalRoleFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.TypeFilter

import static com.codebrig.arthur.observe.structure.naming.util.NamingUtils.trimTrailingComma

/**
 * Used to get the names of PHP AST nodes
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 * @author <a href="mailto:valpecaoco@gmail.com">Val Pecaoco</a>
 */
class PhpNaming implements StructureNaming {

    @Override
    boolean isNamedNodeType(String internalType) {
        switch (Objects.requireNonNull(internalType)) {
            case "StmtFunction":
            case "Stmt_Function":
            case "Expr_Assign":
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
                return getFunctionName(node)
            case "Expr_Assign":
                return getExprAssignName(node)
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
                default:
                    break
            }
        }
        functionName = trimTrailingComma(functionName)
        if (hasFunctionName) {
            functionName += ")"
        }
        return functionName
    }

    static String getExprAssignName(SourceNode node) {
        def name = ""
        new TypeFilter("Expr_Variable").getFilteredNodes(node).each {
            MultiFilter.matchAll(
                    new TypeFilter("Name"),
                    new InternalRoleFilter("name")
            ).getFilteredNodes(it.children).each {
                name = it.token
            }
        }
        return name
    }
}
