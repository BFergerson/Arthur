package com.codebrig.omnisrc.observations

import com.codebrig.omnisrc.SourceLanguage
import com.google.common.base.CaseFormat
import gopkg.in.bblfsh.sdk.v1.uast.generated.Node
import gopkg.in.bblfsh.sdk.v1.uast.generated.Role

import java.util.stream.Collectors

/**
 * @author github.com/BFergerson
 * todo: ranked over all globalEntities
 */
class ObservedLanguage {

    public final SourceLanguage language
    public final HashMap<String, ObservedAttributes> attributes
    public final HashMap<String, ObservedRelations> relations
    public final HashMap<String, ObservedRoles> roles
    public final HashMap<String, String> entityExtends
    public final HashMap<String, String> attributeExtends
    public final HashMap<String, String> relationExtends

    ObservedLanguage(SourceLanguage language) {
        this.language = language
        this.attributes = new HashMap<>()
        this.relations = new HashMap<>()
        this.roles = new HashMap<>()
        this.entityExtends = new HashMap<>()
        this.attributeExtends = new HashMap<>()
        this.relationExtends = new HashMap<>()
    }

    void removeEntityRole(String entity, String role) {
        roles.get(entity).removeObservation(role)
    }

    void observeAttributes(String entity, Map<String, String> entityAttributes) {
        entity = toValidEntity(entity)
        def cleanedAttributes = new HashMap<>()
        entityAttributes.each {
            if (it.key != "internalRole" && it.key != "token") {
                cleanedAttributes.put(toValidAttribute(it.key), it.value)
            }
        }
        attributes.putIfAbsent(entity, new ObservedAttributes())
        attributes.get(entity).observe(cleanedAttributes)
    }

    void observeRelations(String entity, Iterator<Node> entityChildren) {
        entity = toValidEntity(entity)
        relations.putIfAbsent(entity, new ObservedRelations())
        entityChildren.each { child ->
            if (child.properties().get("internalRole").isDefined()
                    && !child.internalType().isEmpty()) { //todo: understand this clause
                //parent relates to child (has)
                relations.get(entity).observeHas(toValidRelation(child.properties().get("internalRole").get()))

                //child relates to parent (is)
                def childEntity = toValidEntity(child.internalType())
                relations.putIfAbsent(childEntity, new ObservedRelations())
                relations.get(childEntity).observeIs(toValidRelation(child.properties().get("internalRole").get()))
            }
        }
    }

    void observeRoles(String entity, Iterator<Role> entityRoles) {
        entity = toValidEntity(entity)
        roles.putIfAbsent(entity, new ObservedRoles())
        roles.get(entity).observe(entityRoles.toList().stream()
                .map({ it -> it.name() })
                .collect(Collectors.toList()).iterator())
    }

    void addEntityExtends(String entity) {
        entityExtends.put(language.qualifiedName + entity, entity)
    }

    String getEntity(String entity, boolean multilingual) {
        if (isOmnilingual() || !multilingual) {
            return entity
        }
        return language.qualifiedName + entity
    }

    String getEntityExtends(String entity) {
        return entityExtends.getOrDefault(entity, language.qualifiedName + "SourceArtifact")
    }

    void addAttributeExtends(String attribute) {
        attributeExtends.put(language.key() + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, attribute), attribute)
    }

    String getAttribute(String attribute, boolean multilingual) {
        if (isOmnilingual() || !multilingual) {
            return attribute
        }
        return language.key() + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, attribute)
    }

    String getAttributeExtends(String attribute) {
        return attributeExtends.getOrDefault(attribute, "attribute")
    }

    void addRelationExtends(String relation) {
        relationExtends.put(language.key() + "_" + relation, relation)
    }

    String getRelation(String relation, boolean multilingual) {
        if (isOmnilingual() || !multilingual) {
            return relation
        }
        return language.key() + "_" + relation
    }

    String getRelationExtends(String relation) {
        return relationExtends.getOrDefault(relation, "relationship")
    }

    List<String> getObservedEntities() {
        return attributes.keySet().toList()
    }

    List<String> getObservedAttributes() {
        return attributes.values().collect({ it.rankedAttributes })
                .flatten().toSet().toList() as List<String>
    }

    List<String> getObservedRoles() {
        return roles.values().collect { it.rankedRoles }
                .flatten().toSet().toList() as List<String>
    }

    List<String> getObservedRelations() {
        return relations.values().collect { it.rankedIsRelations + it.rankedHasRelations }
                .flatten().toSet().toList() as List<String>
    }

    List<String> getEntitiesWithRole(String role) {
        def roles = new ArrayList<String>()
        this.roles.each {
            if (it.value.rankedRoles.contains(role)) {
                roles.add(it.key)
            }
        }
        return roles
    }

    boolean observedEntity(String entity) {
        return observedEntities.contains(entity)
    }

    boolean observedAttribute(String attribute) {
        return observedAttributes.contains(attribute)
    }

    boolean observedEntityRole(String entity, String role) {
        if (!roles.containsKey(entity)) {
            return false
        }
        return roles.get(entity).rankedRoles.contains(role)
    }

    boolean observedRelation(String relation) {
        return observedRelations.contains(relation)
    }

    boolean isOmnilingual() {
        return language == SourceLanguage.OmniSRC
    }

    static String toValidEntity(String entity) {
        //ex. EntityName
        entity = entity.replace("?", "")
        entity = handleBreaker(".", entity)
        entity = handleBreaker("_", entity)
        entity = entity.substring(0, 1).toUpperCase() + entity.substring(1)
        return entity + "Artifact"
    }

    static String toValidAttribute(String attribute) {
        //ex. attributeName
        attribute = handleBreaker(".", attribute)
        attribute = handleBreaker("_", attribute)
        attribute = attribute.substring(0, 1).toLowerCase() + attribute.substring(1)
        return attribute + "Attribute"
    }

    static String toValidRelation(String relation) {
        //ex. relation_name
        relation = relation.replace("@", "")
        int breakerIndex
        while ((breakerIndex = indexOfUCL(relation)) != -1) {
            def sb = new StringBuilder(relation)
            sb.setCharAt(breakerIndex, Character.toLowerCase(sb.charAt(breakerIndex)))
            sb.insert(breakerIndex, "_")
            relation = sb.toString()
        }
        if (relation.startsWith("_")) {
            relation = relation.substring(1)
        }
        return relation + "_relation"
    }

    private static String handleBreaker(String breaker, String value) {
        int breakerIndex
        while ((breakerIndex = value.indexOf(breaker)) != -1) {
            def sb = new StringBuilder(value)
            sb.deleteCharAt(breakerIndex)
            sb.setCharAt(breakerIndex, Character.toUpperCase(sb.charAt(breakerIndex)))
            value = sb.toString()
        }
        return value
    }

    private static int indexOfUCL(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (Character.isUpperCase(str.charAt(i))) {
                return i
            }
        }
        return -1
    }
}
