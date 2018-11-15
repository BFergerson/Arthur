package com.codebrig.omnisrc.observe.filter

import com.codebrig.omnisrc.SourceNode
import com.codebrig.omnisrc.SourceNodeFilter

/**
 * Match by the token
 *
 * @version 0.2
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class TokenFilter extends SourceNodeFilter {

    private final String token

    TokenFilter(String token) {
        this.token = token
    }

    @Override
    boolean evaluate(SourceNode node) {
        if (node != null) {
            return node.token == token
        }
        return false
    }
}
