package com.codebrig.arthur.observe.structure.naming

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureNaming
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.RoleFilter
import com.codebrig.arthur.observe.structure.filter.TypeFilter

/**
 * Used to get the names/qualified names of Bash AST nodes
 *
 * @version 0.4
 * @since 0.4
 * @author <a href="mailto:valpecaoco@gmail.com"> Val Pecaoco</a>
 */
class BashNaming implements StructureNaming {

    @Override
    boolean isNamedNodeType(String internalType) {
        switch (Objects.requireNonNull(internalType)) {
            case "function-def-element":
            case "var-def-element":
                return true
            default:
                return false
        }
    }

    @Override
    String getNodeName(SourceNode node) {
        switch (Objects.requireNonNull(node).internalType) {
            case "function-def-element":
                return getFunctionDefElementName(node)
            case "var-def-element":
                return getVarDefElementName(node)
            default:
                throw new IllegalArgumentException("Unsupported Bash node type: " + node.internalType)
        }
    }

    static String getFunctionDefElementName(SourceNode node) {
        def name = ""
        new TypeFilter("named_symbol").getFilteredNodes(node.children).each {
            name += MultiFilter.matchAll(
                    new RoleFilter("EXPRESSION"), new RoleFilter("IDENTIFIER"))
                    .getFilteredNodes(it.children).next().token
        }
        return name + "()"
    }

    static String getVarDefElementName(SourceNode node) {
        def name = ""
        new TypeFilter("assignment_word").getFilteredNodes(node.children).each {
            name += it.token
        }
        return name
    }
}
