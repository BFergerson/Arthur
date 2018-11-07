package com.codebrig.omnisrc

import com.codebrig.omnisrc.structure.StructureName
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

    static SourceNode getSourceNode(SourceLanguage language, Node node) {
        return getSourceNode(language, node, node)
    }

    static SourceNode getSourceNode(SourceLanguage language, Node rootNode, Node underlyingNode) {
        def sourceNode = sourceNodes.get(underlyingNode)
        if (sourceNode == null) {
            sourceNodes.put(underlyingNode, new SourceNode(language, rootNode, underlyingNode))
        }
        return sourceNodes.get(underlyingNode)
    }

    private static final Map<Node, SourceNode> sourceNodes = new IdentityHashMap<>()
    private final SourceLanguage language
    private final Node rootNode
    private final Node underlyingNode

    private SourceNode(SourceLanguage language, Node rootNode, Node underlyingNode) {
        this.language = Objects.requireNonNull(language)
        this.rootNode = Objects.requireNonNull(rootNode)
        this.underlyingNode = Objects.requireNonNull(underlyingNode)
    }

    SourceLanguage getLanguage() {
        return language
    }

    SourceNode getRootSourceNode() {
        return getSourceNode(language, rootNode)
    }

    Node getUnderlyingNode() {
        return underlyingNode
    }

    String getInternalType() {
        return underlyingNode.internalType()
    }

    String getQualifiedName() {
        return StructureName.getNodeName(this)
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
            return getSourceNode(language, this.rootNode, node)
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
}
