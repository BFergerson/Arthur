package com.codebrig.arthur.observe.structure

import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.SourceNode
import gopkg.in.bblfsh.sdk.v1.uast.generated.Node
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
        return getFilteredNodes(language, node, true)
    }

    Iterator<SourceNode> getFilteredNodes(SourceLanguage language, Node node, boolean onChildren) {
        return getFilteredNodes(new SourceNode(language, node), onChildren)
    }

    Iterator<SourceNode> getFilteredNodes(SourceNode sourceNode) {
        return getFilteredNodes(sourceNode, true)
    }

    Iterator<SourceNode> getFilteredNodes(SourceNode sourceNode, boolean onChildren) {
        if (onChildren) {
            return new FilterIterator(new ChildPreorderIterator(sourceNode), this)
        } else {
            return new FilterIterator(new ParentIterator(sourceNode), this)
        }
    }

    static class ChildPreorderIterator implements Iterator<SourceNode> {

        private final Deque<SourceNode> nodeStack

        ChildPreorderIterator(SourceNode node) {
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

    static class ParentIterator implements Iterator<SourceNode> {

        private final Deque<SourceNode> nodeStack

        ParentIterator(SourceNode node) {
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
            if (next.parentSourceNode != null) {
                nodeStack.push(next.parentSourceNode)
            }
            return next
        }
    }
}
