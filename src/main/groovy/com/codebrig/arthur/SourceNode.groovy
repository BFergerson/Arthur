package com.codebrig.arthur

import com.codebrig.arthur.observe.structure.StructureLiteral
import com.codebrig.arthur.observe.structure.StructureNaming
import gopkg.in.bblfsh.sdk.v1.uast.role.generated.Role
import org.apache.commons.collections4.iterators.TransformIterator
import org.bblfsh.client.v2.JNode
import scala.collection.JavaConverters

/**
 * Represents a single UAST unit extracted from source code.
 *
 * @version 0.4
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class SourceNode {

    private final SourceLanguage language
    private final JNode rootNode
    private final JNode underlyingNode
    private final SourceNode parentSourceNode
    private final StructureNaming naming
    private final StructureLiteral literal

    SourceNode(SourceLanguage language, JNode underlyingNode) {
        this(language, underlyingNode, underlyingNode)
    }

    SourceNode(SourceLanguage language, JNode rootNode, JNode underlyingNode) {
        this(language, rootNode, underlyingNode, null)
    }

    SourceNode(SourceLanguage language, JNode rootNode, JNode underlyingNode, SourceNode parentNode) {
        this.language = Objects.requireNonNull(language)
        this.rootNode = Objects.requireNonNull(rootNode)
        this.underlyingNode = Objects.requireNonNull(underlyingNode)
        this.parentSourceNode = parentNode
        this.naming = language.structureNaming
        this.literal = language.structureLiteral
    }

    SourceLanguage getLanguage() {
        return language
    }

    JNode getRootNode() {
        return rootNode
    }

    SourceNode getRootSourceNode() {
        return new SourceNode(language, rootNode)
    }

    JNode getUnderlyingNode() {
        return underlyingNode
    }

    SourceNode getParentSourceNode() {
        return parentSourceNode
    }

    String getInternalType() {
        return underlyingNode.internalType()
    }

    String getName() {
        return naming.getNodeName(this)
    }

    String getToken() {
        if (underlyingNode.properties().contains("token")) {
            return underlyingNode.properties().get("token").get()
        }
        return underlyingNode.token()
    }

    Iterator<SourceNode> getChildren() {
        def itr = asJavaIterator(underlyingNode.children())
        return new TransformIterator<Node, SourceNode>(itr, { Node node ->
            if (node == null) {
                return null
            }
            return new SourceNode(language, this.rootNode, node, this)
        })
    }

    Map<String, String> getProperties() {
        return asJavaMap(underlyingNode.properties())
    }

    Iterator<Role> getRoles() {
        return asJavaIterator(underlyingNode.roles())
    }

    boolean isLiteralNode() {
        return literal.isNodeLiteral(this)
    }

    String getLiteralAttribute() {
        return literal.getNodeLiteralAttribute(this)
    }

    List<String> getPossibleLiteralAttributes() {
        return literal.getPossibleNodeLiteralAttributes(this)
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
