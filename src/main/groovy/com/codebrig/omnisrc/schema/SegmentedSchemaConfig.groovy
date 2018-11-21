package com.codebrig.omnisrc.schema

import groovy.transform.Canonical

/**
 * Allows for segmenting the generated schema into multiple files by SchemaSegment
 *
 * @version 0.3
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

    Map<File, Set<SchemaSegment>> getSchemaSegmentsByFile() {
        def rtnMap = new HashMap<File, Set<SchemaSegment>>()
        fileOutputs.each {
            rtnMap.putIfAbsent(it.value, new HashSet<SchemaSegment>())
            rtnMap.get(it.value).add(it.key)
        }
        return rtnMap
    }
}
