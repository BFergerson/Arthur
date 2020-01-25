package com.codebrig.arthur.observe.structure.filter

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter

/**
 * Match by the fully qualified name
 *
 * @version 0.4
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 * @author <a href="mailto:valpecaoco@gmail.com"> Val Pecaoco</a>
 */
class NameFilter extends StructureFilter<NameFilter, String> {

    NameFilter(String... values) {
        accept(values)
    }

    @Override
    boolean evaluate(SourceNode node) {
        if (node != null && node.hasName()) {
            def name = node.name
            if (name.contains("(")) {
                return evaluateProperty(name) || evaluateProperty(name.substring(0, name.indexOf("(")))
            } else {
                return evaluateProperty(name)
            }
        } else {
            if (node?.internalType == "var-def-element") {
                def matched = new TypeFilter("assignment_word").getFilteredNodes(node.children)
                if (matched.hasNext()) {
                    return evaluateProperty(matched.next().token)
                }
            }
            return false
        }
    }
}
