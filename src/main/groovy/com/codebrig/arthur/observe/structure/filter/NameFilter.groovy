package com.codebrig.arthur.observe.structure.filter

import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter

/**
 * Match by the name
 *
 * @version 0.4
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class NameFilter extends StructureFilter<NameFilter, String> {

    NameFilter(String... values) {
        accept(values)
    }

    @Override
    boolean evaluate(SourceNode node) {
        if (node != null) {
            if (node.language in [SourceLanguage.Python, SourceLanguage.Ruby]) {
                return evaluateProperty(node.token)
            }

            def childNameFilter = null
            if (node.language == SourceLanguage.Javascript) {
                switch (Objects.requireNonNull(node).internalType) {
                    case "FunctionDeclaration":
                    case "FunctionExpression":
                    case "ArrowFunctionExpression":
                        childNameFilter = new InternalRoleFilter("id").getFilteredNodes(node.children)
                        break
                    case "ObjectMethod":
                        childNameFilter = MultiFilter.matchAll(
                                new TypeFilter("Identifier"), new InternalRoleFilter("key")
                        ).getFilteredNodes(node.children)
                        break
                    case "NewExpression":
                        childNameFilter = MultiFilter.matchAll(
                                new TypeFilter("Identifier"), new InternalRoleFilter("callee")
                        ).getFilteredNodes(node.children)
                        break
                    default:
                        break
                }
            } else if (node.language == SourceLanguage.Java) {
                if (node.internalType == "VariableDeclarationStatement") {
                    MultiFilter.matchAll(
                            new InternalRoleFilter("fragments"),
                            new TypeFilter("VariableDeclarationFragment")
                    ).getFilteredNodes(node).each {
                        childNameFilter = MultiFilter.matchAll(
                                new InternalRoleFilter("name"),
                                new TypeFilter().reject("VariableDeclarationFragment")
                        ).getFilteredNodes(it.children)
                    }
                }

                if (childNameFilter == null) {
                    if (node.internalType == "VariableDeclarationFragment") {
                        return false
                    }

                    childNameFilter = MultiFilter.matchAll(
                            new InternalRoleFilter("name"),
                            new TypeFilter().reject("VariableDeclarationFragment")
                    ).getFilteredNodes(node.children)
                }
            } else {
                childNameFilter = new InternalRoleFilter("name").getFilteredNodes(node.children)
            }
            return new TokenFilter()
                    .accept(acceptSet.toArray(new String[0]))
                    .reject(rejectSet.toArray(new String[0]))
                    .getFilteredNodes(childNameFilter).hasNext()
        }
        return false
    }
}
