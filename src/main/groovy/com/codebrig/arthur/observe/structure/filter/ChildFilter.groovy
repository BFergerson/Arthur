package com.codebrig.arthur.observe.structure.filter

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter

/**
 * Match by child
 *
 * @version 0.4
 * @since 0.4
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class ChildFilter extends StructureFilter<ChildFilter, StructureFilter> {

    static ChildFilter matchAny(StructureFilter... filters) {
        return new ChildFilter(MatchStyle.ANY, filters)
    }

    static ChildFilter matchAll(StructureFilter... filters) {
        return new ChildFilter(MatchStyle.ALL, filters)
    }

    private final MatchStyle matchStyle

    ChildFilter(MatchStyle matchStyle) {
        this.matchStyle = Objects.requireNonNull(matchStyle)
    }

    ChildFilter(MatchStyle matchStyle, StructureFilter... filters) {
        this.matchStyle = Objects.requireNonNull(matchStyle)
        accept(filters)
    }

    MatchStyle getMatchStyle() {
        return matchStyle
    }

    @Override
    boolean evaluate(SourceNode node) {
        if (node == null || node.children == null || !node.children.hasNext()) {
            return false
        }
        if (matchStyle == MatchStyle.ANY) {
            return acceptSet.any { f -> node.children.any { f.evaluate(it) } } &&
                    (rejectSet.isEmpty() || !rejectSet.any { f -> node.children.any { f.evaluate(it) } })
        } else {
            return acceptSet.every { f -> node.children.any { f.evaluate(it) } } &&
                    (rejectSet.isEmpty() || !rejectSet.every { f -> node.children.any { f.evaluate(it) } })
        }
    }

    static enum MatchStyle {
        ANY,
        ALL
    }
}
