package com.codebrig.arthur.observe.structure.naming

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureNaming
import com.codebrig.arthur.observe.structure.filter.InternalRoleFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.TypeFilter

import static com.codebrig.arthur.observe.structure.naming.util.NamingUtils.trimTrailingComma

/**
 * Used to get the names of Go AST nodes
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 * @author <a href="mailto:valpecaoco@gmail.com">Val Pecaoco</a>
 */
class GoNaming extends StructureNaming {

    @Override
    boolean isNamedNodeType(String internalType) {
        switch (Objects.requireNonNull(internalType)) {
            case "FuncDecl":
            case "AssignStmt":
            case "GenDecl":
                return true
            default:
                return false
        }
    }

    @Override
    String getNodeName(SourceNode node) {
        switch (Objects.requireNonNull(node).internalType) {
            case "FuncDecl":
                return getFuncDeclName(node)
            case "AssignStmt":
                return getAssignStmtName(node)
            case "GenDecl":
                return getGenDeclName(node)
            default:
                return super.getNodeName(node)
        }
    }

    static String getFuncDeclName(SourceNode node) {
        def functionName = ""
        new InternalRoleFilter("Name").getFilteredNodes(node.children).each {
            functionName = it.token
            functionName += "("
            new TypeFilter("FuncType").getFilteredNodes(node.children).each {
                new TypeFilter("FieldList").getFilteredNodes(it.children).each {
                    new TypeFilter("Field").getFilteredNodes(it.children).each {
                        MultiFilter.matchAll(
                                new TypeFilter("Ident", "ArrayType", "Ellipsis", "StarExpr"),
                                new InternalRoleFilter("Type")
                        ).getFilteredNodes(it.children).each {
                            switch (it.internalType) {
                                case "Ident":
                                    functionName += getIdentTypeName(it)
                                    break
                                case "ArrayType":
                                    functionName += getArrayTypeName(it)
                                    break
                                case "Ellipsis":
                                    functionName += getEllipsisTypeName(it)
                                    break
                                case "StarExpr":
                                    functionName += getStarExprTypeName(it)
                                    break
                                default:
                                    throw new IllegalStateException("Unsupported function argument node type: " + it.internalType)
                            }
                        }
                    }
                }
            }
            functionName = trimTrailingComma(functionName)
            functionName += ")"
        }
        return functionName
    }

    static String getAssignStmtName(SourceNode node) {
        def name = ""
        new TypeFilter("Ident").getFilteredNodes(node.children).each {
            name += it.token
        }
        return name
    }

    static String getGenDeclName(SourceNode node) {
        def name = ""
        new TypeFilter("ValueSpec").getFilteredNodes(node).each {
            MultiFilter.matchAll(
                    new TypeFilter("Ident"),
                    new InternalRoleFilter("Names")
            ).getFilteredNodes(it.children).each {
                name += it.token
            }
        }
        return name
    }

    static String getIdentTypeName(SourceNode node) {
        def name = ""
        new TypeFilter("Ident").getFilteredNodes(node).each {
            name += it.token + ","
        }
        return name
    }

    static String getArrayTypeName(SourceNode node) {
        def name = ""
        def isArray = false
        MultiFilter.matchAll(
                new TypeFilter("BasicLit", "Ident"),
                new InternalRoleFilter("Len", "Elt")
        ).getFilteredNodes(node.children).each {
            def token = it.token
            if (it.internalType == "BasicLit") {
                name += ("[" + token + "]")
                isArray = true
            } else if (it.internalType == "Ident") {
                def identName = getIdentTypeName(it)
                if (!isArray) {
                    identName = ("[]" + identName)
                }
                name += identName
            }
        }
        return name
    }

    static String getEllipsisTypeName(SourceNode node) {
        def name = ""
        MultiFilter.matchAll(
                new TypeFilter("Ident"),
                new InternalRoleFilter("Elt")
        ).getFilteredNodes(node.children).each {
            name += "..."
            name += getIdentTypeName(it)
        }
        return name
    }

    static String getStarExprTypeName(SourceNode node) {
        def name = ""
        MultiFilter.matchAll(
                new TypeFilter("Ident"),
                new InternalRoleFilter("X")
        ).getFilteredNodes(node.children).each {
            name += "*"
            name += getIdentTypeName(it)
        }
        return name
    }
}
