package com.codebrig.omnisrc.observe.structure.naming

import com.codebrig.omnisrc.SourceNode
import com.codebrig.omnisrc.observe.structure.StructureNaming

/**
 * Used to get the names of Python AST nodes
 *
 * @version 0.3
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class PythonNaming implements StructureNaming {

    @Override
    String getNodeName(SourceNode node) {
        switch (Objects.requireNonNull(node).internalType) {
            case "FunctionDef":
                return getFunctionDefName(node)
            default:
                throw new IllegalArgumentException("Unsupported Python node type: " + node.internalType)
        }
    }

    static String getFunctionDefName(SourceNode node) {
        def functionName = node.token
        return functionName + "()"
    }
}
