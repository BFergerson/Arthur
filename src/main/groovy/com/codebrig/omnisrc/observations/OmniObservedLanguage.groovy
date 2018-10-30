package com.codebrig.omnisrc.observations

import com.codebrig.omnisrc.SourceLanguage

/**
 * @author github.com/BFergerson
 */
class OmniObservedLanguage extends ObservedLanguage {

    //todo: rename to omniEntities?
    public final Set<String> globalEntities
    public final Set<String> globalAttributes
    public final Set<String> globalRelations
    public final Set<String> globalRoles

    OmniObservedLanguage() {
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
    List<String> getObservedEntities() {
        return globalEntities.toList()
    }

    @Override
    List<String> getObservedAttributes() {
        return globalAttributes.toList()
    }

    @Override
    List<?> getObservedRelations() {
        return globalRelations.toList()
    }

    @Override
    List<String> getObservedRoles() {
        return globalRoles.toList()
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
