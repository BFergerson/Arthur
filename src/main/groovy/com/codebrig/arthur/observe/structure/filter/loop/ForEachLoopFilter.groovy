package com.codebrig.arthur.observe.structure.filter.loop

import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.structure.StructureFilter
import com.codebrig.arthur.observe.structure.filter.MultiFilter
import com.codebrig.arthur.observe.structure.filter.RoleFilter
import com.codebrig.arthur.observe.structure.filter.TypeFilter

/**
 * Match by for each loop
 *
 * @version 0.4
 * @since 0.3
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 * @author <a href="mailto:valpecaoco@gmail.com">Val Pecaoco</a>
 */
class ForEachLoopFilter extends StructureFilter<ForEachLoopFilter, Void> {

    private final MultiFilter filter

    ForEachLoopFilter() {
        filter = MultiFilter.matchAny(
                MultiFilter.matchAll(
                        new RoleFilter("FOR"), new RoleFilter("STATEMENT"), new RoleFilter("ITERATOR"),
                        new RoleFilter().reject("DECLARATION", "VARIABLE")
                ),
                new TypeFilter("ForEachKeyword"), new TypeFilter("for_shellcommand"),
                //todo: remove following line (https://github.com/bblfsh/cpp-driver/pull/58)
                new TypeFilter("CPPASTRangeBasedForStatement")
        )
    }

    @Override
    boolean evaluate(SourceNode node) {
        boolean result = filter.evaluate(node)
        if (result) {
            if (node.internalType == "for_shellcommand") {
                return evaluateForShellCommand(node)
            } else {
                return true
            }
        }
        return result
    }

    static boolean evaluateForShellCommand(SourceNode node) {
        def a = new TypeFilter("for_shellcommand").getFilteredNodes(node)
        if (a?.hasNext()) {
            def b = MultiFilter.matchAll(
                    new RoleFilter("FOR"), new RoleFilter("EXPRESSION"), new RoleFilter("ITERATOR"),
                    new RoleFilter("DECLARATION"), new RoleFilter("VARIABLE")
            ).getFilteredNodes(a.next().children)
            if (b?.hasNext()) {
                return true
            }
        }
        return false
    }
}
