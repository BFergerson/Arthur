package com.codebrig.omnisrc.observe.filter

import com.codebrig.omnisrc.SourceNodeFilter
import com.codebrig.omnisrc.SourceLanguage
import com.codebrig.omnisrc.SourceNode

/**
 * todo: description
 *
 * @version 0.2
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class LanguageFilter extends SourceNodeFilter {

    private final SourceLanguage language

    LanguageFilter(SourceLanguage language) {
        this.language = Objects.requireNonNull(language)
    }

    @Override
    boolean evaluate(SourceNode node) {
        if (node != null) {
            return node.language == language
        }
        return false
    }
}
