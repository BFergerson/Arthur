package com.codebrig.arthur.observe.structure.filter

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter

/**
 * Match by multiple filters
 *
 * @version 0.4
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class MultiFilter extends StructureFilter<MultiFilter, StructureFilter> {

    static MultiFilter matchAny(StructureFilter... filters) {
        return new MultiFilter(MatchStyle.ANY, filters)
    }

    static MultiFilter matchAll(StructureFilter... filters) {
        return new MultiFilter(MatchStyle.ALL, filters)
    }

    private final MatchStyle matchStyle

    MultiFilter(MatchStyle matchStyle) {
        this.matchStyle = Objects.requireNonNull(matchStyle)
    }

    MultiFilter(MatchStyle matchStyle, StructureFilter... filters) {
        this.matchStyle = Objects.requireNonNull(matchStyle)
        accept(filters)
    }

    MatchStyle getMatchStyle() {
        return matchStyle
    }

    @Override
    boolean evaluate(SourceNode object) {
        if (matchStyle == MatchStyle.ANY) {
            return (acceptSet.isEmpty() || acceptSet.any { it.evaluate(object) }) &&
                    (rejectSet.isEmpty() || !rejectSet.any { it.evaluate(object) })
        } else {
            return (acceptSet.isEmpty() || acceptSet.every { it.evaluate(object) }) &&
                    (rejectSet.isEmpty() || !rejectSet.every { it.evaluate(object) })
        }
    }

    static enum MatchStyle {
        ANY,
        ALL
    }
}
