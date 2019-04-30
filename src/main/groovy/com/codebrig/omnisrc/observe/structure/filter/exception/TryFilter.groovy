package com.codebrig.omnisrc.observe.structure.filter.exception

import com.codebrig.omnisrc.SourceNode
import com.codebrig.omnisrc.SourceNodeFilter

/**
 * Match by try in exception handling construct
 *
 * @version 0.3.2
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class TryFilter extends SourceNodeFilter<TryFilter, Void> {

    private static final Set<String> exceptionTypes = new HashSet<>()
    static {
        exceptionTypes.add("TryExcept") //python
        exceptionTypes.add("TryStatement") //java, javascript
    }

    @Override
    boolean evaluate(SourceNode node) {
        return node != null && node.internalType in exceptionTypes
    }
}
