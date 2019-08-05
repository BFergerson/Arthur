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
                return true
            default:
                return false
        }
    }

    @Override
    String getNodeName(SourceNode node) {
        switch (Objects.requireNonNull(node).internalType) {
            case "CPPASTFunctionDeclarator":
                return getFunctionDeclaratorName(node)
            default:
                throw new IllegalArgumentException("Unsupported C++ node type: " + node.internalType)
        }
    }

    static String getFunctionDeclaratorName(SourceNode node) {
        def name = getAstName(node.children)
        name += "("
        node.children.each {
            if (it.internalType == "CPPASTDeclarator") {
                new TypeFilter("CPPASTDeclarator").getFilteredNodes(it).each {
                    new TypeFilter("CPPASTSimpleDeclSpecifier", "CPPASTNamedTypeSpecifier").getFilteredNodes(it.children).each {
                        if (it.internalType == "CPPASTNamedTypeSpecifier") {
                            def matched = new TypeFilter("CPPASTQualifiedName").getFilteredNodes(it.children)
                            if (matched.hasNext()) {
                                it.children.each {
                                    new TypeFilter("CPPASTTemplateId").getFilteredNodes(it.children).each {
                                        new TypeFilter("CPPASTTypeId").getFilteredNodes(it.children).each {
                                            new TypeFilter("CPPASTSimpleDeclSpecifier").getFilteredNodes(it.children).each {
                                                name += it.token + ","
                                            }
                                        }
                                    }
                                }
                            } else {
                                name += getAstName(it.children) + ","
                            }
                        } else {
                            name += it.token + ","
                        }
                    }
                }
            } else if (it.internalType == "CPPASTArrayDeclarator") {
                new TypeFilter("CPPASTArrayDeclarator").getFilteredNodes(it).each {
                    new TypeFilter("CPPASTSimpleDeclSpecifier").getFilteredNodes(it.children).each {
                        name += it.token + ","
                    }
                }
            }
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
