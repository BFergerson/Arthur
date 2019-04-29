package com.codebrig.omnisrc.observe

import com.codebrig.omnisrc.SourceLanguage
import com.codebrig.omnisrc.observe.observations.ObservedRoles
import com.codebrig.omnisrc.schema.SchemaSegment

/**
 * Represents multiple observed languages
 *
 * @version 0.3.2
 * @since 0.1
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class ObservedLanguages extends ObservedLanguage {

    static ObservedLanguages mergeLanguages(ObservedLanguage... observedLanguages) {
        return mergeLanguages(Arrays.asList(observedLanguages))
    }

    static ObservedLanguages mergeLanguages(List<ObservedLanguage> observedLanguages) {
        def segments = new HashSet<SchemaSegment>()
        observedLanguages.each {
            segments.addAll(it.config.observedSegments)
        }
        def omniLanguage = new ObservedLanguages(new ObservationConfig(segments.toArray(new SchemaSegment[0])))
        observedLanguages.each { lang ->
            lang.observedEntities.each { entity ->
                observedLanguages.stream().each {
                    if (lang.language != it.language && it.observedEntity(entity)) {
                        omniLanguage.observeGlobalEntity(entity)
                        observedLanguages.each {
                            it.addEntityExtends(entity)
                        }
                    }
                }
            }
            lang.observedAttributes.each { attribute ->
                observedLanguages.stream().each {
                    if (lang.language != it.language && it.observedAttribute(attribute)) {
                        omniLanguage.observeGlobalAttribute(attribute)
                        observedLanguages.each {
                            it.addAttributeExtends(attribute)
                        }
                    }
                }
            }
            lang.observedRelations.each { relation ->
                observedLanguages.stream().each {
                    if (lang.language != it.language && it.observedRelation(relation)) {
                        omniLanguage.observeGlobalRelation(relation)
                        observedLanguages.each {
                            it.addRelationExtends(relation)
                        }
                    }
                }
            }
            lang.getObservedRoles(true, true, false).each {
                omniLanguage.observeGlobalIndividualRole(it)
            }
            lang.getObservedRoles(true, false, true).each {
                omniLanguage.observeGlobalActualRole(it)
            }
        }
        omniLanguage.getObservedRoles(true, true, false).each { role ->
            observedLanguages.each { lang ->
                lang.getEntitiesWithRole(role).each { entity ->
                    observedLanguages.each {
                        if (lang.language != it.language && it.observedEntityRole(entity, role)) {
                            omniLanguage.addEntityIndividualRole(entity, role)
                            it.removeEntityRole(entity, role)
                            lang.removeEntityRole(entity, role)
                        }
                    }
                }
            }
        }
        omniLanguage.getObservedRoles(true, false, true).each { role ->
            observedLanguages.each { lang ->
                lang.getEntitiesWithRole(role).each { entity ->
                    observedLanguages.each {
                        if (lang.language != it.language && it.observedEntityRole(entity, role)) {
                            omniLanguage.addEntityActualRole(entity, role)
                            it.removeEntityRole(entity, role)
                            lang.removeEntityRole(entity, role)
                        }
                    }
                }
            }
        }
        return omniLanguage
    }

    //todo: rename to omniEntities?
    final Set<String> globalEntities
    final Set<String> globalAttributes
    final Set<String> globalRelations
    final Set<String> globalIndividualRoles
    final Set<String> globalActualRoles

    private ObservedLanguages(ObservationConfig observationConfig) {
        super(SourceLanguage.OmniSRC, observationConfig)
        this.globalEntities = new HashSet<>()
        this.globalAttributes = new HashSet<>()
        this.globalRelations = new HashSet<>()
        this.globalIndividualRoles = new HashSet<>()
        this.globalActualRoles = new HashSet<>()
    }

    void addEntityIndividualRole(String entity, String role) {
        roles.putIfAbsent(entity, new ObservedRoles())
        roles.get(entity).observe([role].iterator(), false)
    }

    void addEntityActualRole(String entity, String role) {
        roles.putIfAbsent(entity, new ObservedRoles())
        roles.get(entity).observeActual(role)
    }

    void observeGlobalEntity(String entity) {
        globalEntities.add(entity)
    }

    void observeGlobalAttribute(String attribute) {
        globalAttributes.add(attribute)
    }

    void observeGlobalRelation(String relation) {
        globalRelations.add(relation)
    }

    void observeGlobalIndividualRole(String role) {
        globalIndividualRoles.add(role)
    }

    void observeGlobalActualRole(String role) {
        globalActualRoles.add(role)
    }

    @Override
    List<String> getObservedEntities(boolean naturalOrdering) {
        if (naturalOrdering) {
            def rtnEntities = globalEntities.toList()
            rtnEntities.sort(String.CASE_INSENSITIVE_ORDER)
            return rtnEntities
        } else {
            return globalEntities.toList()
        }
    }

    @Override
    List<String> getObservedAttributes(boolean naturalOrdering) {
        if (naturalOrdering) {
            def rtnAttributes = globalAttributes.toList()
            rtnAttributes.sort(String.CASE_INSENSITIVE_ORDER)
            return rtnAttributes
        } else {
            return globalAttributes.toList()
        }
    }

    @Override
    List<String> getObservedRoles(boolean naturalOrdering, boolean includeIndividual, boolean includeActual) {
        def globalRoles = new HashSet<String>()
        if (includeIndividual) {
            globalRoles.addAll(globalIndividualRoles)
        }
        if (includeActual) {
            globalRoles.addAll(globalActualRoles)
        }

        if (naturalOrdering) {
            def rtnRoles = globalRoles.toList()
            rtnRoles.sort(String.CASE_INSENSITIVE_ORDER)
            return rtnRoles
        } else {
            return globalRoles.toList()
        }
    }

    @Override
    List<String> getObservedRelations(boolean naturalOrdering) {
        if (naturalOrdering) {
            def rtnRelations = globalRelations.toList()
            rtnRelations.sort(String.CASE_INSENSITIVE_ORDER)
            return rtnRelations
        } else {
            return globalRelations.toList()
        }
    }

    @Override
    boolean observedEntity(String entity) {
        return globalEntities.contains(entity)
    }

    @Override
    boolean observedAttribute(String attribute) {
        return globalAttributes.contains(attribute)
    }

    @Override
    boolean observedRelation(String relation) {
        return globalRelations.contains(relation)
    }
}
