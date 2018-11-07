package com.codebrig.omnisrc.structure.filter

import gopkg.in.bblfsh.sdk.v1.uast.generated.Node

/**
 * todo: description
 *
 * @version 0.2
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class WildcardFilter implements StructureFilter {
    @Override
    boolean evaluate(Node object) {
        return true
    }
}
