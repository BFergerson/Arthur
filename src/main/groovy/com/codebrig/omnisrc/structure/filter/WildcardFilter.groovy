package com.codebrig.omnisrc.structure.filter

import com.codebrig.omnisrc.SourceNode
import com.codebrig.omnisrc.structure.StructureFilter

/**
 * todo: description
 *
 * @version 0.2
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class WildcardFilter implements StructureFilter {
    @Override
    boolean evaluate(SourceNode object) {
        return true
    }
}
