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

    private final List<SourceFilter> filters
    private MatchStyle matchStyle

    MultiFilter() {
        this.filters = new ArrayList<>()
        this.matchStyle = MatchStyle.ANY
    }

    MultiFilter(SourceFilter... filters) {
        this.filters = Arrays.asList(filters)
        this.matchStyle = MatchStyle.ANY
    }

    void setMatchStyle(MatchStyle matchStyle) {
        this.matchStyle = Objects.requireNonNull(matchStyle)
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
