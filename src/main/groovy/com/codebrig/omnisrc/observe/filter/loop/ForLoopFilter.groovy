package com.codebrig.omnisrc.observe.filter.loop

import com.codebrig.omnisrc.SourceNode
import com.codebrig.omnisrc.SourceNodeFilter

/**
 * todo: this
 *
 * @version 0.3
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class ForLoopFilter extends SourceNodeFilter<ForLoopFilter, Void> {

    private static final Set<String> loopTypes = new HashSet<>()
    static {
        loopTypes.add("ForStmt") //go
        loopTypes.add("ForStatement") //java, javascript, python
    }

    @Override
    boolean evaluate(SourceNode node) {
        return node != null && node.internalType in loopTypes
    }
}
