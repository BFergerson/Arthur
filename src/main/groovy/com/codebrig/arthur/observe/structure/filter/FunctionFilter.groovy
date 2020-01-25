package com.codebrig.arthur.observe.structure.filter

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter

/**
 * Match by function declaration
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class FunctionFilter extends StructureFilter<FunctionFilter, Void> {

    private final MultiFilter filter

    FunctionFilter() {
        filter = MultiFilter.matchAny(
                new TypeFilter("uast:FunctionGroup"),
                MultiFilter.matchAll(
                        new RoleFilter("DECLARATION"), new RoleFilter("FUNCTION"),
                        new RoleFilter().reject("ARGUMENT", "RETURN", "INCOMPLETE", "BODY")
                )
        )
    }

    @Override
    boolean evaluate(SourceNode node) {
        return filter.evaluate(node)
    }
}
