package com.codebrig.omnisrc

import com.codebrig.omnisrc.observe.structure.StructureNaming
import gopkg.in.bblfsh.sdk.v1.uast.generated.Node
import gopkg.in.bblfsh.sdk.v1.uast.generated.Role
import org.apache.commons.collections4.iterators.TransformIterator
import scala.collection.JavaConverters

/**
 * Represents a single UAST unit extracted from source code.
 *
 * @version 0.2
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class SourceNode {

    private final SourceLanguage language
    private final Node rootNode
    private final Node underlyingNode
    private final StructureNaming naming

    SourceNode(SourceLanguage language, Node underlyingNode) {
        this(language, underlyingNode, underlyingNode)
    }

    SourceNode(SourceLanguage language, Node rootNode, Node underlyingNode) {
        this.language = Objects.requireNonNull(language)
        this.rootNode = Objects.requireNonNull(rootNode)
        this.underlyingNode = Objects.requireNonNull(underlyingNode)
        this.naming = language.structureNaming
    }

    SourceLanguage getLanguage() {
        return language
    }

    Node getRootNode() {
        return rootNode
    }

    SourceNode getRootSourceNode() {
        return new SourceNode(language, rootNode)
    }

    Node getUnderlyingNode() {
        return underlyingNode
    }

    String getInternalType() {
        return underlyingNode.internalType()
    }

    String getQualifiedName() {
        return naming.getNodeName(this)
    }

    String getToken() {
        return underlyingNode.token()
    }

    Iterator<SourceNode> getChildren() {
        def itr = asJavaIterator(underlyingNode.children())
        return new TransformIterator<Node, SourceNode>(itr, { Node node ->
            if (node == null) {
                return null
            }
            return new SourceNode(language, this.rootNode, node)
        })
    }

    Map<String, String> getProperties() {
        return asJavaMap(underlyingNode.properties())
    }

    Iterator<Role> getRoles() {
        return asJavaIterator(underlyingNode.roles())
    }

    @Override
    String toString() {
        return internalType + " (" + language + ")"
    }

    static <T> Iterator<T> asJavaIterator(scala.collection.Iterable<T> scalaIterator) {
        return JavaConverters.asJavaCollectionConverter(scalaIterator).asJavaCollection().iterator()
    }

    static Map<String, String> asJavaMap(scala.collection.Map<String, String> scalaMap) {
        return JavaConverters.mapAsJavaMapConverter(scalaMap).asJava()
    }
}
