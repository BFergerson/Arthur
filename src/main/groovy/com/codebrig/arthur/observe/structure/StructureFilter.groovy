package com.codebrig.arthur.observe.structure

import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.SourceNode
import org.apache.commons.collections4.Predicate
import org.apache.commons.collections4.iterators.FilterIterator

/**
 * Used to filter through SourceNodes
 *
 * @version 0.4
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
abstract class StructureFilter<T extends StructureFilter, P> implements Predicate<SourceNode> {

    protected final Set<P> acceptSet = new LinkedHashSet<>()
    protected final Set<P> rejectSet = new LinkedHashSet<>()

    T accept(P... values) {
        Objects.requireNonNull(values).each {
            acceptSet.add(Objects.requireNonNull(it))
        }
        return (T) this
    }

    T reject(P... values) {
        Objects.requireNonNull(values).each {
            rejectSet.add(Objects.requireNonNull(it))
        }
        return (T) this
    }

    boolean evaluateProperty(P value) {
        return (acceptSet.isEmpty() || acceptSet.contains(value)) && !rejectSet.contains(value)
    }

    Iterator<SourceNode> getFilteredNodes(Iterator<SourceNode> sourceNodes) {
        return new FilterIterator(sourceNodes, this)
    }

    Iterator<SourceNode> getFilteredNodes(SourceLanguage language, Node node) {
        return getFilteredNodes(new SourceNode(language, node))
    }

    Iterator<SourceNode> getFilteredNodes(SourceNode sourceNode) {
        return new FilterIterator(new PreorderIterator(sourceNode), this)
    }

    static class PreorderIterator implements Iterator<SourceNode> {

        private final Deque<SourceNode> nodeStack

        PreorderIterator(SourceNode node) {
            nodeStack = new ArrayDeque<SourceNode>()
            nodeStack.push(node)
        }

        @Override
        boolean hasNext() {
            return !nodeStack.isEmpty()
        }

        @Override
        SourceNode next() {
            SourceNode next = nodeStack.pop()
            next.children.reverse().each {
                nodeStack.push(it)
            }
            return next
        }
    }
}
