package com.codebrig.omnisrc.schema

import groovy.transform.Canonical

/**
 * todo: description
 *
 * @version 0.2
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
@Canonical
class SegmentedSchemaConfig {
    Map<SchemaSegment, File> fileOutputs = new HashMap<>()

    SegmentedSchemaConfig withFileSegment(File outputFile, SchemaSegment... segments) {
        segments.each {
            fileOutputs.put(it, Objects.requireNonNull(outputFile))
        }
        return this
    }
}
