package com.codebrig.omnisrc.structure.filter

import gopkg.in.bblfsh.sdk.v1.uast.generated.Node
import org.apache.commons.collections4.Predicate
import org.apache.commons.collections4.iterators.FilterIterator
import org.bblfsh.client.BblfshClient
import scala.collection.JavaConverters

/**
 * todo: description
 *
 * @version 0.2
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
trait StructureFilter implements Predicate<Node> {

    Iterator<Node> getFilteredNodes(scala.collection.Iterable<Node> uastNodes) {
        return new FilterIterator(asJavaIterator(uastNodes), this)
    }

    Iterator<Node> getFilteredNodes(Node uastNode) {
        return new FilterIterator(asJavaIterator(BblfshClient.iterator(uastNode, BblfshClient.PreOrder())), this)
    }

    static <T> Iterator<T> asJavaIterator(scala.collection.Iterator<T> scalaIterator) {
        return JavaConverters.asJavaIteratorConverter(scalaIterator).asJava()
    }

    static <T> Iterator<T> asJavaIterator(scala.collection.Iterable<T> scalaIterator) {
        return JavaConverters.asJavaCollectionConverter(scalaIterator).asJavaCollection().iterator()
    }
}
