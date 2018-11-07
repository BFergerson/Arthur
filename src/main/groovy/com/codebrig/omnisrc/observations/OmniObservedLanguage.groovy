package com.codebrig.omnisrc.observations

import com.codebrig.omnisrc.SourceLanguage

/**
 * todo: description
 *
 * @version 0.2
 * @since 0.1
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class OmniObservedLanguage extends ObservedLanguage {

    static OmniObservedLanguage makeOmniObservedLanguage(ObservedLanguage... observedLanguages) {
        return makeOmniObservedLanguage(Arrays.asList(observedLanguages))
    }

    static OmniObservedLanguage makeOmniObservedLanguage(List<ObservedLanguage> observedLanguages) {
        def omniLanguage = new OmniObservedLanguage()
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
            lang.observedRoles.each { role ->
                omniLanguage.observeGlobalRole(role)
            }
        }
        omniLanguage.observedRoles.each { role ->
            observedLanguages.each { lang ->
                lang.getEntitiesWithRole(role).each { entity ->
                    observedLanguages.each {
                        if (lang.language != it.language && it.observedEntityRole(entity, role)) {
                            omniLanguage.addEntityRole(entity, role)
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
    public final Set<String> globalEntities
    public final Set<String> globalAttributes
    public final Set<String> globalRelations
    public final Set<String> globalRoles

    private OmniObservedLanguage() {
        super(SourceLanguage.OmniSRC)
        this.globalEntities = new HashSet<>()
        this.globalAttributes = new HashSet<>()
        this.globalRelations = new HashSet<>()
        this.globalRoles = new HashSet<>()
    }

    void addEntityRole(String entity, String role) {
        roles.putIfAbsent(entity, new ObservedRoles())
        roles.get(entity).observe([role].iterator())
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

    void observeGlobalRole(String role) {
        globalRoles.add(role)
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
    List<String> getObservedRoles(boolean naturalOrdering) {
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
