package com.codebrig.omnisrc

import gopkg.in.bblfsh.sdk.v1.uast.generated.Node
import org.apache.commons.collections4.Predicate
import org.apache.commons.collections4.iterators.FilterIterator
import org.apache.commons.collections4.iterators.TransformIterator
import org.bblfsh.client.BblfshClient
import scala.collection.JavaConverters

/**
 * Used to filter through SourceNodes
 *
 * @version 0.3
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
abstract class SourceNodeFilter implements Predicate<SourceNode> {

    Iterator<SourceNode> getFilteredNodes(Iterator<SourceNode> sourceNodes) {
        return new FilterIterator(sourceNodes, this)
    }

    Iterator<SourceNode> getFilteredNodes(SourceLanguage language, Node node) {
        return getFilteredNodes(new SourceNode(language, node))
    }

    Iterator<SourceNode> getFilteredNodes(SourceNode sourceNode) {
        return getFilteredNodes(sourceNode, BblfshClient.PreOrder())
    }

    Iterator<SourceNode> getFilteredNodes(SourceNode sourceNode, int sortMethod) {
        def itr = asJavaIterator(BblfshClient.iterator(sourceNode.underlyingNode, sortMethod))
        def transformItr = new TransformIterator<Node, SourceNode>(itr, { Node node ->
            if (node == null) {
                return null
            }
            return new SourceNode(sourceNode.language, sourceNode.underlyingNode, node)
        })
        return new FilterIterator(transformItr, this)
    }

    static <T> Iterator<T> asJavaIterator(scala.collection.Iterator<T> scalaIterator) {
        return JavaConverters.asJavaIteratorConverter(scalaIterator).asJava()
    }
}
