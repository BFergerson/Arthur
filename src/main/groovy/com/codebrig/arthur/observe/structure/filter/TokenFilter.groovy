package com.codebrig.arthur.observe.structure.filter

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter

/**
 * Match by the token
 *
 * @version 0.3.2
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class TokenFilter extends StructureFilter<TokenFilter, String> {

    TokenFilter(String... values) {
        accept(values)
    }

    @Override
    boolean evaluate(SourceNode node) {
        if (node != null) {
            return evaluateProperty(node.token)
        }
        return false
    }
}
