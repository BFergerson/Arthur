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
class MultiFilter extends SourceNodeFilter {

    static MultiFilter matchAny(SourceNodeFilter... filters) {
        return new MultiFilter(MatchStyle.ANY, filters)
    }

    static MultiFilter matchAll(SourceNodeFilter... filters) {
        return new MultiFilter(MatchStyle.ALL, filters)
    }

    private final MatchStyle matchStyle
    private final List<SourceNodeFilter> filters

    MultiFilter(MatchStyle matchStyle) {
        this.matchStyle = Objects.requireNonNull(matchStyle)
        this.filters = new ArrayList<>()
    }

    MultiFilter(MatchStyle matchStyle, SourceNodeFilter... filters) {
        this.matchStyle = Objects.requireNonNull(matchStyle)
        this.filters = Arrays.asList(filters)
    }

    MatchStyle getMatchStyle() {
        return matchStyle
    }

    void acceptFilter(SourceNodeFilter filter) {
        filters.add(Objects.requireNonNull(filter))
    }

    @Override
    boolean evaluate(SourceNode object) {
        if (matchStyle == MatchStyle.ANY) {
            return filters.any { it.evaluate(object) }
        } else {
            return filters.every { it.evaluate(object) }
        }
    }

    static enum MatchStyle {
        ANY,
        ALL
    }
}
