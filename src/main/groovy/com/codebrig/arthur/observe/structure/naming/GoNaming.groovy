package com.codebrig.arthur.observe.structure.naming

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureNaming
import com.codebrig.arthur.observe.structure.filter.InternalRoleFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.TypeFilter

/**
 * Used to get the names of Go AST nodes
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class GoNaming implements StructureNaming {

    @Override
    boolean isNamedNodeType(String internalType) {
        switch (Objects.requireNonNull(internalType)) {
            case "FuncDecl":
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
            default:
                throw new IllegalArgumentException("Unsupported Go node type: " + node.internalType)
        }
    }

    static String getFuncDeclName(SourceNode node) {
        def name = ""
        new InternalRoleFilter("Name").getFilteredNodes(node.children).each {
            name = it.token
            name += "("
            new TypeFilter("FuncType").getFilteredNodes(node.children).each {
                new TypeFilter("FieldList").getFilteredNodes(it.children).each {
                    new TypeFilter("Field").getFilteredNodes(it.children).each {
                        MultiFilter.matchAll(
                                new TypeFilter("Ident", "ArrayType", "Ellipsis", "StarExpr"),
                                new InternalRoleFilter("Type")
                        ).getFilteredNodes(it.children).each {
                            switch (it.internalType) {
                                case "Ident":
                                    name += getIdentTypeName(it)
                                    break
                                case "ArrayType":
                                    def isArray = false
                                    MultiFilter.matchAll(
                                            new TypeFilter("BasicLit", "Ident"),
                                            new InternalRoleFilter("Len", "Elt")
                                    ).getFilteredNodes(it.children).each {
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
                                    break
                                case "Ellipsis":
                                    MultiFilter.matchAll(
                                            new TypeFilter("Ident"),
                                            new InternalRoleFilter("Elt")
                                    ).getFilteredNodes(it.children).each {
                                        name += "..."
                                        name += getIdentTypeName(it)
                                    }
                                    break
                                case "StarExpr":
                                    MultiFilter.matchAll(
                                            new TypeFilter("Ident"),
                                            new InternalRoleFilter("X")
                                    ).getFilteredNodes(it.children).each {
                                        name += "*"
                                        name += getIdentTypeName(it)
                                    }
                                    break
                                default:
                                    throw new IllegalStateException("Unsupported Go node type: " + it.internalType)
                            }
                        }
                    }
                }
            }
            if (name.endsWith(",")) {
                name = name.substring(0, name.length() - 1)
            }
            name += ")"
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
}
