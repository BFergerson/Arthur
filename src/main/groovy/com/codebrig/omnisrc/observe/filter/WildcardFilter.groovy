package com.codebrig.omnisrc.observe.filter

import com.codebrig.omnisrc.SourceNodeFilter
import com.codebrig.omnisrc.SourceNode

/**
 * todo: description
 *
 * @version 0.2
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class WildcardFilter extends SourceNodeFilter {

    @Override
    boolean evaluate(SourceNode object) {
        return true
    }
}
