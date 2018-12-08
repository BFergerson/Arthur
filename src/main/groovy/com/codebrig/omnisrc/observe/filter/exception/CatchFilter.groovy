package com.codebrig.omnisrc.observe.filter.exception

import com.codebrig.omnisrc.SourceNode
import com.codebrig.omnisrc.SourceNodeFilter

/**
 * Match by catch in exception handling construct
 *
 * @version 0.3
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class CatchFilter extends SourceNodeFilter<CatchFilter, Void> {

    private static final Set<String> exceptionTypes = new HashSet<>()
    static {
        exceptionTypes.add("ExceptHandler") //python
        exceptionTypes.add("CatchClause") //java, javascript
    }

    @Override
    boolean evaluate(SourceNode node) {
        return node != null && node.internalType in exceptionTypes
    }
}
