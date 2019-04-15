package com.codebrig.omnisrc.observe.filter

import com.codebrig.omnisrc.SourceNode
import com.codebrig.omnisrc.SourceNodeFilter

/**
 * Match by multiple filters
 *
 * @version 0.3.1
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class MultiFilter extends SourceNodeFilter<MultiFilter, SourceNodeFilter> {

    static MultiFilter matchAny(SourceNodeFilter... filters) {
        return new MultiFilter(MatchStyle.ANY, filters)
    }

    static MultiFilter matchAll(SourceNodeFilter... filters) {
        return new MultiFilter(MatchStyle.ALL, filters)
    }

    private final MatchStyle matchStyle

    MultiFilter(MatchStyle matchStyle) {
        this.matchStyle = Objects.requireNonNull(matchStyle)
    }

    MultiFilter(MatchStyle matchStyle, SourceNodeFilter... filters) {
        this.matchStyle = Objects.requireNonNull(matchStyle)
        accept(filters)
    }

    MatchStyle getMatchStyle() {
        return matchStyle
    }

    @Override
    boolean evaluate(SourceNode object) {
        if (matchStyle == MatchStyle.ANY) {
            return acceptSet.any { it.evaluate(object) } &&
                    (rejectSet.isEmpty() || !rejectSet.any { it.evaluate(object) })
        } else {
            return acceptSet.every { it.evaluate(object) } &&
                    (rejectSet.isEmpty() || !rejectSet.every { it.evaluate(object) })
        }
    }

    static enum MatchStyle {
        ANY,
        ALL
    }
}
