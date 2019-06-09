package com.codebrig.arthur.observe.structure.naming

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureNaming
import com.codebrig.arthur.observe.structure.filter.InternalRoleFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.TypeFilter
import com.codebrig.arthur.util.Util
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Used to get the names of JavaScript AST nodes
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class JavascriptNaming implements StructureNaming {

    private static final Logger log = LoggerFactory.getLogger(this.name)

    @Override
    boolean isNamedNodeType(String internalType) {
        switch (Objects.requireNonNull(internalType)) {
            case "FunctionDeclaration":
            case "FunctionExpression":
            case "ArrowFunctionExpression":
            case "ObjectMethod":
            case "NewExpression":
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
            case "FunctionExpression":
                return getFunctionExpressionName(node)
            case "ArrowFunctionExpression":
                return getArrowFunctionExpressionName(node)
            case "ObjectMethod":
                return getObjectMethodName(node)
            case "NewExpression":
                return getNewExpressionName(node)
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

    static String getFunctionExpressionName(SourceNode node) {
        log.info "function expression"
        def functionName = ""
        new InternalRoleFilter("id").getFilteredNodes(node.children).each {
            functionName = it.token
        }
        functionName += assembleParamNames(node)
        return functionName
    }

    static String getArrowFunctionExpressionName(SourceNode node) {
        log.info "arrow expression"
        def functionName = ""
        new InternalRoleFilter("id").getFilteredNodes(node.children).each {
            functionName = it.token
        }
        functionName += assembleParamNames(node)
        return functionName
    }

    static String getObjectMethodName(SourceNode node) {
        def functionName = ""
        MultiFilter.matchAll(
                new TypeFilter("Identifier"), new InternalRoleFilter("key")
        ).getFilteredNodes(node.children).each {
            functionName = it.token
        }
        functionName += assembleParamNames(node)
        return functionName
    }

    static String getNewExpressionName(SourceNode node) {
        def functionName = ""
        MultiFilter.matchAll(
                new TypeFilter("Identifier"), new InternalRoleFilter("callee")
        ).getFilteredNodes(node.children).each {
            functionName = it.token
        }
        functionName += "("
        functionName += getArgumentNames(node)
        functionName = Util.trimTrailingComma(functionName)
        functionName += ")"
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
            }
        }
        name = Util.trimTrailingComma(name)
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
