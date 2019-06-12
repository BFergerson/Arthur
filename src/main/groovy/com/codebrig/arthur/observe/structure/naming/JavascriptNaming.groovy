package com.codebrig.arthur.observe.structure.naming

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureNaming
import com.codebrig.arthur.observe.structure.filter.InternalRoleFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.TypeFilter

import static com.codebrig.arthur.observe.structure.naming.util.NamingUtils.trimTrailingComma

/**
 * Used to get the names of JavaScript AST nodes
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 * @author <a href="mailto:valpecaoco@gmail.com">Val Pecaoco</a>
 */
class JavascriptNaming implements StructureNaming {

    @Override
    boolean isNamedNodeType(String internalType) {
        switch (Objects.requireNonNull(internalType)) {
            case "FunctionDeclaration":
                return true
            default:
                return false
        }
    }

    @Override
    String getNodeName(SourceNode node) {
        switch (Objects.requireNonNull(node).internalType) {
            case "FunctionDeclaration":
                return getFunctionDeclarationName(node)
            default:
                return null
        }
    }

    static String getFunctionDeclarationName(SourceNode node) {
        def functionName = ""
        new InternalRoleFilter("id").getFilteredNodes(node.children).each {
            functionName = it.token
        }
        functionName += assembleParamNames(node)
        return functionName
    }

    static String assembleParamNames(SourceNode node) {
        def name = "("
        MultiFilter.matchAll(
                new TypeFilter("Identifier", "AssignmentPattern", "RestElement"),
                new InternalRoleFilter("params", "argument", "arguments")
        ).getFilteredNodes(node.children).each {
            switch (it.internalType) {
                case "Identifier":
                    name += getIdentifierParamNames(it)
                    break
                case "AssignmentPattern":
                    name += getAssignmentParamNames(it)
                    break
                case "RestElement":
                    name += getArgumentNames(it)
                    break
                default:
                    throw new IllegalStateException("Unsupported function argument node type: " + it.internalType)
            }
        }
        name = trimTrailingComma(name)
        name += ")"
        return name
    }

    static String getIdentifierParamNames(SourceNode node) {
        def name = ""
        def paramsMatched = MultiFilter.matchAll(
                new InternalRoleFilter("params")
        ).getFilteredNodes(node)
        if (paramsMatched.hasNext()) {
            name += getRegularParamNames(node)
        }
        def argumentsMatched = MultiFilter.matchAll(
                new InternalRoleFilter("arguments")
        ).getFilteredNodes(node)
        if (argumentsMatched.hasNext()) {
            name += getArgumentNames(node)
        }
        return name
    }

    static String getRegularParamNames(SourceNode node) {
        def name = ""
        new InternalRoleFilter("params").getFilteredNodes(node).each {
            name += it.token + ","
        }
        return name
    }

    static String getArgumentNames(SourceNode node) {
        def name = ""
        new InternalRoleFilter("argument", "arguments").getFilteredNodes(node).each {
            name += it.token + ","
        }
        return name
    }

    static String getAssignmentParamNames(SourceNode node) {
        def name = ""
        new InternalRoleFilter("left").getFilteredNodes(node.children).each {
            name += it.token + ","
        }
        return name
    }
}
