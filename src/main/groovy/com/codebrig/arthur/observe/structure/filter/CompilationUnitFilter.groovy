package com.codebrig.arthur.observe.structure.filter

import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter

/**
 * Match by compilation unit
 *
 * @version 0.4
 * @since 0.4
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class CompilationUnitFilter extends StructureFilter<CompilationUnitFilter, Void> {

    private final MultiFilter filter

    CompilationUnitFilter() {
        filter = MultiFilter.matchAny(
                new RoleFilter("FILE"),
                MultiFilter.matchAll(
                        new RoleFilter("MODULE"),
                        new LanguageFilter().reject(SourceLanguage.Javascript)
                )
        )
    }

    @Override
    boolean evaluate(SourceNode node) {
        return filter.evaluate(node)
    }
}
