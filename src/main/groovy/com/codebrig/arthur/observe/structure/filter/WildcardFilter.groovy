package com.codebrig.arthur.observe.structure.filter

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter

/**
 * Matches everything
 *
 * @version 0.3.2
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class WildcardFilter extends StructureFilter<WildcardFilter, Void> {

    @Override
    boolean evaluate(SourceNode object) {
        return true
    }
}
