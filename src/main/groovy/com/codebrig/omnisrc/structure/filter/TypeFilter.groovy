package com.codebrig.omnisrc.structure.filter

import gopkg.in.bblfsh.sdk.v1.uast.generated.Node
import org.apache.commons.collections4.iterators.FilterIterator
import org.bblfsh.client.BblfshClient

/**
 * todo: description
 *
 * @version 0.2
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class TypeFilter implements StructureFilter {

    private Set<String> acceptedTypes

    TypeFilter() {
        acceptedTypes = new HashSet<>()
    }

    TypeFilter(String... acceptTypes) {
        acceptedTypes = new HashSet<>(Arrays.asList(acceptTypes))
    }

    void acceptType(String internalType) {
        acceptedTypes.add(internalType)
    }

    @Override
    Iterator<Node> getFilteredNodes(Node uastNodes) {
        return new FilterIterator(asJavaIterator(BblfshClient.iterator(uastNodes, BblfshClient.PreOrder())), this)
    }

    @Override
    boolean evaluate(Node node) {
        return acceptedTypes.contains(node?.internalType())

    }
}
