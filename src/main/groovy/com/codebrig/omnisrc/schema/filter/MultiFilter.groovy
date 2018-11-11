package com.codebrig.omnisrc.schema.filter

import com.codebrig.omnisrc.SourceFilter
import com.codebrig.omnisrc.SourceNode

/**
 * todo: description
 *
 * @version 0.2
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class MultiFilter extends SourceFilter {

    static MultiFilter matchAny(SourceFilter... filters) {
        return new MultiFilter(MatchStyle.ANY, filters)
    }

    static MultiFilter matchAll(SourceFilter... filters) {
        return new MultiFilter(MatchStyle.ALL, filters)
    }

    private final MatchStyle matchStyle
    private final List<SourceFilter> filters

    MultiFilter(MatchStyle matchStyle) {
        this.matchStyle = Objects.requireNonNull(matchStyle)
        this.filters = new ArrayList<>()
    }

    MultiFilter(MatchStyle matchStyle, SourceFilter... filters) {
        this.matchStyle = Objects.requireNonNull(matchStyle)
        this.filters = Arrays.asList(filters)
    }

    MatchStyle getMatchStyle() {
        return matchStyle
    }

    void acceptFilter(SourceFilter filter) {
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
