package com.codebrig.arthur.observe

import com.codebrig.arthur.schema.SchemaSegment
import groovy.transform.Canonical

import static com.codebrig.arthur.schema.SchemaSegment.*

/**
 * Used to configure the schema segments which will be observed
 *
 * @version 0.3.2
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
@Canonical
class ObservationConfig {

    final Set<SchemaSegment> observedSegments = new HashSet<>()

    ObservationConfig(SchemaSegment... segments) {
        segments.each {
            observedSegments.add(it)
        }
    }

    SchemaSegment[] asArray() {
        return observedSegments.toArray(new SchemaSegment[0])
    }

    boolean observingSemanticRoles() {
        return SEMANTIC_ROLES in observedSegments
    }

    static ObservationConfig fullStructure() {
        return new ObservationConfig(values())
    }

    static ObservationConfig baseStructureWithSemanticRoles() {
        def config = new ObservationConfig()
        config.observedSegments.add(ATTRIBUTES)
        config.observedSegments.add(ENTITIES)
        config.observedSegments.add(RELATIONSHIPS)
        config.observedSegments.add(SEMANTIC_ROLES)
        return config
    }

    static ObservationConfig baseStructure() {
        def config = new ObservationConfig()
        config.observedSegments.add(ATTRIBUTES)
        config.observedSegments.add(ENTITIES)
        config.observedSegments.add(RELATIONSHIPS)
        return config
    }
}
