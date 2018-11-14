package com.codebrig.omnisrc.schema

/**
 * todo: description
 *
 * @version 0.2
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
trait SchemaWriter {

    void storeFullSchemaDefinition(File outputFile) {
        if (outputFile.exists()) {
            outputFile.delete()
        } else {
            outputFile.mkdirs()
        }
        outputFile.createNewFile()
        outputFile << fullSchemaDefinition
    }

    abstract void storeSegmentedSchemaDefinition(SegmentedSchemaConfig segmentConfig)

    abstract String getFullSchemaDefinition()
}
