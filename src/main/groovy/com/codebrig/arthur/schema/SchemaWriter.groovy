package com.codebrig.arthur.schema

/**
 * Used to output an ObservedLanguage as a viable schema
 *
 * @version 0.4
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
trait SchemaWriter {

    void storeFullSchemaDefinition(File outputFile) {
        if (outputFile.exists()) {
            outputFile.delete()
        } else {
            outputFile.parentFile.mkdirs()
        }
        outputFile.createNewFile()
        outputFile << fullSchemaDefinition
    }

    void storeSegmentedSchemaDefinition(SegmentedSchemaConfig segmentConfig) {
        Objects.requireNonNull(segmentConfig).schemaSegmentsByFile.each {
            if (it.key.exists()) {
                it.key.delete()
            } else {
                it.key.parentFile.mkdirs()
            }
            it.key << getSegmentedSchemaDefinition(it.value.toArray(new SchemaSegment[0]))
        }
    }

    abstract String getSegmentedSchemaDefinition(SchemaSegment... segments)

    abstract String getFullSchemaDefinition()
}
