package com.codebrig.omnisrc

import gopkg.in.bblfsh.sdk.v1.uast.generated.Node
import gopkg.in.bblfsh.sdk.v1.uast.generated.Role
import org.apache.commons.collections4.iterators.TransformIterator
import scala.collection.JavaConverters

/**
 * todo: description
 *
 * @version 0.2
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class SourceNode {

    private final SourceLanguage language
    private final Node underlyingNode

    SourceNode(SourceLanguage language, Node underlyingNode) {
        this.language = Objects.requireNonNull(language)
        this.underlyingNode = Objects.requireNonNull(underlyingNode)
    }

    SourceLanguage getLanguage() {
        return language
    }

    Node getUnderlyingNode() {
        return underlyingNode
    }

    String getInternalType() {
        return underlyingNode.internalType()
    }

    Iterator<SourceNode> getChildren() {
        def itr = asJavaIterator(underlyingNode.children())
        return new TransformIterator<Node, SourceNode>(itr, { Node node ->
            if (node == null) {
                return null
            }
            return new SourceNode(language, node)
        })
    }

    Map<String, String> getProperties() {
        return asJavaMap(underlyingNode.properties())
    }

    Iterator<Role> getRoles() {
        return asJavaIterator(underlyingNode.roles())
    }

    static <T> Iterator<T> asJavaIterator(scala.collection.Iterable<T> scalaIterator) {
        return JavaConverters.asJavaCollectionConverter(scalaIterator).asJavaCollection().iterator()
    }

    static Map<String, String> asJavaMap(scala.collection.Map<String, String> scalaMap) {
        return JavaConverters.mapAsJavaMapConverter(scalaMap).asJava()
    }

    static <T> Iterator<T> asJavaIterator(scala.collection.Iterator<T> scalaIterator) {
        return JavaConverters.asJavaIteratorConverter(scalaIterator).asJava()
    }
}
