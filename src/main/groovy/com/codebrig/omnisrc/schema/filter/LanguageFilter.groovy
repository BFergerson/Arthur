package com.codebrig.omnisrc.schema.filter

import com.codebrig.omnisrc.SourceFilter
import com.codebrig.omnisrc.SourceLanguage
import com.codebrig.omnisrc.SourceNode

/**
 * todo: description
 *
 * @version 0.2
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class LanguageFilter extends SourceFilter {

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
