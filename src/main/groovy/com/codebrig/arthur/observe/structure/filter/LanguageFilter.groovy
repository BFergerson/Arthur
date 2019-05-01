package com.codebrig.arthur.observe.structure.filter

import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.SourceNodeFilter

/**
 * Match by the source code language
 *
 * @version 0.3.2
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class LanguageFilter extends SourceNodeFilter<LanguageFilter, SourceLanguage> {

    LanguageFilter(SourceLanguage... values) {
        accept(values)
    }

    @Override
    boolean evaluate(SourceNode node) {
        if (node != null) {
            return evaluateProperty(node.language)
        }
        return false
    }
}
