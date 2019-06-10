package com.codebrig.arthur.observe.structure.naming

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureNaming
import com.codebrig.arthur.observe.structure.filter.InternalRoleFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.TypeFilter

import static com.codebrig.arthur.observe.structure.naming.util.NamingUtils.trimTrailingComma

/**
 * Used to get the names of Ruby AST nodes
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 * @author <a href="mailto:valpecaoco@gmail.com">Val Pecaoco</a>
 */
class RubyNaming implements StructureNaming {

    @Override
    boolean isNamedNodeType(String internalType) {
        switch (Objects.requireNonNull(internalType)) {
            case "Def":
            case "def":
                return true
            default:
                return false
        }
    }

    @Override
    String getNodeName(SourceNode node) {
        switch (Objects.requireNonNull(node).internalType) {
            case "Def":
            case "def":
                return getDefName(node)
            default:
                return null
        }
    }

    static String getDefName(SourceNode node) {
        def functionName = node.token
        functionName += "("
        MultiFilter.matchAll(
                new TypeFilter("args"),
                new InternalRoleFilter("args")
        ).getFilteredNodes(node.children).each {
            it.children.each {
                functionName += it.token + ","
            }
        }
        functionName = trimTrailingComma(functionName)
        functionName += ")"
        return functionName
    }
}
