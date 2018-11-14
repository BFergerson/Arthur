package com.codebrig.omnisrc.schema

/**
 * todo: description
 *
 * @version 0.2
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
interface SchemaWriter {
    void storeFullSchemaDefinition(File outputFile)

    void storeSegmentedSchemaDefinition(SegmentedSchemaConfig segmentConfig)

    String getSchemaDefinition()
}
