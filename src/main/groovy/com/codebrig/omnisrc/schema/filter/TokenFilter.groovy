package com.codebrig.omnisrc.schema.filter

import com.codebrig.omnisrc.SourceFilter
import com.codebrig.omnisrc.SourceNode

/**
 * todo: description
 *
 * @version 0.2
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class TokenFilter extends SourceFilter {

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
