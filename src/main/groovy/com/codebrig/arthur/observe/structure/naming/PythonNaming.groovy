package com.codebrig.arthur.observe.structure.naming

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureNaming
import com.codebrig.arthur.observe.structure.filter.InternalRoleFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.TypeFilter

import static com.codebrig.arthur.observe.structure.naming.util.NamingUtils.trimTrailingComma

/**
 * Used to get the names of Python AST nodes
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 * @author <a href="mailto:valpecaoco@gmail.com">Val Pecaoco</a>
 */
class PythonNaming implements StructureNaming {

    @Override
    boolean isNamedNodeType(String internalType) {
        switch (Objects.requireNonNull(internalType)) {
            case "FunctionDef":
                return true
            default:
                return false
        }
    }

    @Override
    String getNodeName(SourceNode node) {
        switch (Objects.requireNonNull(node).internalType) {
            case "FunctionDef":
                return getFunctionDefName(node)
            default:
                return null
        }
    }

    static String getFunctionDefName(SourceNode node) {
        def functionName = node.token
        functionName += "("
        MultiFilter.matchAll(
                new TypeFilter("arguments"),
                new InternalRoleFilter("args")
        ).getFilteredNodes(node.children).each {
            it.children.each {
                switch (it.internalType) {
                    case "arg":
                    case "kwarg":
                        functionName += it.token + ","
                        break
                    default:
                        throw new IllegalStateException("Unsupported function argument node type: " + it.internalType)
                }
            }
        }
        functionName = trimTrailingComma(functionName)
        functionName += ")"
        return functionName
    }
}
