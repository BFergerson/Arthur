package com.codebrig.arthur.observe.structure.naming

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureNaming
import com.codebrig.arthur.observe.structure.filter.TypeFilter

import static com.codebrig.arthur.observe.structure.naming.util.NamingUtils.trimTrailingComma

/**
 * Used to get the names/qualified names of C# AST nodes
 *
 * @version 0.4
 * @since 0.4
 * @author <a href="mailto:valpecaoco@gmail.com">Val Pecaoco</a>
 */
class CPlusPlusNaming implements StructureNaming {

    @Override
    boolean isNamedNodeType(String internalType) {
        switch (Objects.requireNonNull(internalType)) {
            case "CPPASTFunctionDeclarator":
            // case "CPPASTName":
                return true
            default:
                return false
        }
    }

    @Override
    String getNodeName(SourceNode node) {
        switch (Objects.requireNonNull(node).internalType) {
            case "CPPASTFunctionDeclarator":
                // case "CPPASTName":
                return getFunctionDeclaratorName(node)
            default:
                throw new IllegalArgumentException("Unsupported C++ node type: " + node.internalType)
        }
    }

    static String getFunctionDeclaratorName(SourceNode node) {
        def name = getAstName(node.children)
        name += "("
        new TypeFilter("CPPASTSimpleDeclSpecifier").getFilteredNodes(node).each {
            name += it.token + ","
        }
        name = trimTrailingComma(name)
        return name + ")"
    }

    static String getAstName(Iterator<SourceNode> node) {
        def name = ""
        new TypeFilter("CPPASTName").getFilteredNodes(node).each {
            name = it.token
        }
        return name
    }
}
