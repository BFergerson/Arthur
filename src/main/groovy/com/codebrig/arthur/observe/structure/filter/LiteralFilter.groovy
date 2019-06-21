package com.codebrig.arthur.observe.structure.filter

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter

/**
 * Match by literal value presence
 *
 * @version 0.4
 * @since 0.4
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class LiteralFilter extends StructureFilter<LiteralFilter, Void> {

    @Override
    boolean evaluate(SourceNode node) {
        return node != null && node.isLiteralNode()
    }
}
