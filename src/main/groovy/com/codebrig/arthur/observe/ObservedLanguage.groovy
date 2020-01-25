package com.codebrig.arthur.observe

import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.SourceNode
import com.codebrig.arthur.observe.observations.ObservedAttributes
import com.codebrig.arthur.observe.observations.ObservedRelations
import com.codebrig.arthur.observe.observations.ObservedRoles
import com.codebrig.arthur.observe.structure.StructureLiteral
import com.google.common.base.CaseFormat
import com.google.common.collect.Maps
import gopkg.in.bblfsh.sdk.v1.uast.generated.Role

import java.util.stream.Collectors

/**
 * Extracts the useful information necessary to create
 * a valid schema based on the observed source code.
 *
 * @version 0.4
 * @since 0.1
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class ObservedLanguage {

    static final Set<String> literalAttributes = StructureLiteral.allLiteralAttributes.keySet()
    final SourceLanguage language
    final ObservationConfig config
    final Map<String, ObservedAttributes> attributes
    final Map<String, ObservedRelations> relations
    final Map<String, ObservedRoles> roles
    final Map<String, String> entityExtends
    final Map<String, String> attributeExtends
    final Map<String, String> relationExtends

    ObservedLanguage(SourceLanguage language) {
        this(language, ObservationConfig.baseStructure())
    }

    ObservedLanguage(SourceLanguage language, ObservationConfig config) {
        this.language = Objects.requireNonNull(language)
        this.config = Objects.requireNonNull(config)
        this.attributes = Maps.newConcurrentMap()
        this.relations = Maps.newConcurrentMap()
        this.roles = Maps.newConcurrentMap()
        this.entityExtends = Maps.newConcurrentMap()
        this.attributeExtends = Maps.newConcurrentMap()
        this.relationExtends = Maps.newConcurrentMap()
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

    void observeParentChildRelation(String entity, SourceNode child) {
        entity = toValidEntity(entity)
        relations.putIfAbsent(entity, new ObservedRelations())
        if (!child.properties.get("internalRole")?.isEmpty()
                && !child.internalType.isEmpty()) { //todo: understand this clause (this is phenomena#27)
            //parent is parent
            relations.get(entity).observeIs("parent")

            //child is child
            def childEntity = toValidEntity(child.internalType)
            relations.putIfAbsent(childEntity, new ObservedRelations())
            relations.get(childEntity).observeIs("child")
        }
    }

    void observeRelations(String entity, Iterator<SourceNode> entityChildren) {
        entity = toValidEntity(entity)
        relations.putIfAbsent(entity, new ObservedRelations())
        entityChildren.each { child ->
            if (!child.properties.get("internalRole")?.isEmpty()
                    && !child.internalType.isEmpty()) { //todo: understand this clause (this is phenomena#27)
                //parent relates to child (has)
                relations.get(entity).observeHas(toValidRelation(child.properties.get("internalRole")))

                //child relates to parent (is)
                def childEntity = toValidEntity(child.internalType)
                relations.putIfAbsent(childEntity, new ObservedRelations())
                relations.get(childEntity).observeIs(toValidRelation(child.properties.get("internalRole")))
            }
        }
    }

    void observeRelations(String entity, List<String> entityRelations) {
        entity = toValidEntity(entity)
        relations.putIfAbsent(entity, new ObservedRelations())
        entityRelations.each {
            if (it.startsWith("has")) {
                relations.get(entity).observeHas(it.substring(4))
            } else {
                relations.get(entity).observeIs(it.substring(3))
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

    void observeRoles(String entity, List<String> entityRoles) {
        entity = toValidEntity(entity)
        roles.putIfAbsent(entity, new ObservedRoles())
        roles.get(entity).observe(entityRoles.iterator())
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
        if (!literalAttributes.contains(attribute)) {
            attributeExtends.put(language.key + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, attribute), attribute)
        }
    }

    String getAttribute(String attribute, boolean multilingual) {
        if (isOmnilingual() || !multilingual || literalAttributes.contains(attribute)) {
            return attribute
        }
        return language.key + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, attribute)
    }

    String getAttributeExtends(String attribute) {
        return attributeExtends.getOrDefault(attribute, "attribute")
    }

    void addRelationExtends(String relation) {
        relationExtends.put(language.key + "_" + relation, relation)
    }

    String getRelationRole(String relation, boolean multilingual) {
        def relationRole = getRelation(relation, multilingual)
        if (relationRole.endsWith("_relation")) {
            return relationRole.substring(0, relationRole.length() - 8) + "role"
        }
        return relationRole
    }

    String getRelation(String relation, boolean multilingual) {
        if (isOmnilingual() || !multilingual || relation == "parent" || relation == "child") {
            return relation
        }
        return language.key + "_" + relation
    }

    String getRelationExtends(String relation) {
        return relationExtends.getOrDefault(relation, "parent_child_relation")
    }

    List<String> getObservedEntities() {
        return getObservedEntities(true)
    }

    List<String> getObservedEntities(boolean naturalOrdering) {
        if (naturalOrdering) {
            def rtnEntities = attributes.keySet().toList()
            rtnEntities.sort(String.CASE_INSENSITIVE_ORDER)
            return rtnEntities
        } else {
            return attributes.keySet().toList()
        }
    }

    List<String> getObservedAttributes() {
        return getObservedAttributes(true)
    }

    List<String> getObservedAttributes(boolean naturalOrdering) {
        if (naturalOrdering) {
            def rtnAttributes = attributes.values().collect({ it.attributes })
                    .flatten().toSet().toList() as List<String>
            rtnAttributes.sort(String.CASE_INSENSITIVE_ORDER)
            return rtnAttributes
        } else {
            return attributes.values().collect({ it.rankedAttributes })
                    .flatten().toSet().toList() as List<String>
        }
    }

    List<String> getObservedSemanticRoles() {
        return getObservedSemanticRoles(true)
    }

    List<String> getObservedSemanticRoles(boolean naturalOrdering) {
        if (naturalOrdering) {
            def rtnRoles = roles.values().collect {
                it.getObservedRoles(naturalOrdering)
            }.flatten().toSet().toList() as List<String>
            rtnRoles.sort(String.CASE_INSENSITIVE_ORDER)
            return rtnRoles
        } else {
            return roles.values().collect {
                it.getObservedRoles(naturalOrdering)
            }.flatten().toSet().toList() as List<String>
        }
    }

    List<String> getObservedRelations() {
        return getObservedRelations(true)
    }

    List<String> getObservedRelations(boolean naturalOrdering) {
        if (naturalOrdering) {
            def rtnRelations = relations.values().collect { it.isRelations + it.hasRelations }
                    .flatten().toSet().toList() as List<String>
            rtnRelations.sort(String.CASE_INSENSITIVE_ORDER)
            return rtnRelations
        } else {
            return relations.values().collect { it.rankedIsRelations + it.rankedHasRelations }
                    .flatten().toSet().toList() as List<String>
        }
    }

    List<String> getEntityObservedAttributes(String entity) {
        return getEntityObservedAttributes(entity, true)
    }

    List<String> getEntityObservedAttributes(String entity, boolean naturalOrdering) {
        if (naturalOrdering) {
            def rtnAttributes = attributes.get(entity).attributes
            rtnAttributes.sort(String.CASE_INSENSITIVE_ORDER)
            return rtnAttributes
        } else {
            return attributes.get(entity).rankedAttributes
        }
    }

    List<String> getEntityObservedIsRelations(String entity) {
        return getEntityObservedIsRelations(entity, true)
    }

    List<String> getEntityObservedIsRelations(String entity, boolean naturalOrdering) {
        if (naturalOrdering) {
            def rtnRelations = relations.get(entity).isRelations
            rtnRelations.sort(String.CASE_INSENSITIVE_ORDER)
            return rtnRelations
        } else {
            return relations.get(entity).rankedIsRelations
        }
    }

    List<String> getEntityObservedHasRelations(String entity) {
        return getEntityObservedHasRelations(entity, true)
    }

    List<String> getEntityObservedHasRelations(String entity, boolean naturalOrdering) {
        if (naturalOrdering) {
            def rtnRelations = relations.get(entity).hasRelations
            rtnRelations.sort(String.CASE_INSENSITIVE_ORDER)
            return rtnRelations
        } else {
            return relations.get(entity).rankedHasRelations
        }
    }

    List<String> getEntityObservedRoles(String entity) {
        return getEntityObservedRoles(entity, true)
    }

    List<String> getEntityObservedRoles(String entity, boolean naturalOrdering) {
        if (naturalOrdering) {
            def rtnRoles = roles.get(entity).getObservedRoles(naturalOrdering)
            rtnRoles.sort(String.CASE_INSENSITIVE_ORDER)
            return rtnRoles
        } else {
            return roles.get(entity).getObservedRoles(naturalOrdering)
        }
    }

    List<String> getEntitiesWithRole(String role) {
        return getEntitiesWithRole(role, true)
    }

    List<String> getEntitiesWithRole(String role, boolean naturalOrdering) {
        def rtnRoles = new ArrayList<String>()
        this.roles.each {
            if (it.value.getObservedRoles(naturalOrdering).contains(role)) {
                rtnRoles.add(it.key)
            }
        }
        if (naturalOrdering) {
            rtnRoles.sort(String.CASE_INSENSITIVE_ORDER)
        }
        return rtnRoles
    }

    boolean observedEntity(String entity) {
        return observedEntities.contains(entity)
    }

    boolean observedAttribute(String attribute) {
        return observedAttributes.contains(attribute)
    }

    boolean observedRelation(String relation) {
        return observedRelations.contains(relation)
    }

    boolean observedEntityRole(String entity, String role) {
        if (!roles.containsKey(entity)) {
            return false
        }
        return roles.get(entity).observedRoles.contains(role)
    }

    boolean isOmnilingual() {
        return language == SourceLanguage.Omnilingual
    }

    static String toValidEntity(String entity) {
        //ex. EntityName
        //todo: better
        switch (entity) {
            case "?'":
                entity = "QuestionApostrophe"
                break
            case ">&":
                entity = "GreaterThanAmpersand"
                break
            case "<>":
                entity = "LessThanGreaterThan"
                break
            case "+=":
                entity = "PlusEquals"
                break
            case "=":
                entity = "Equals"
                break
            case "^":
                entity = "Caret"
                break
            case "'":
                entity = "Apostrophe"
                break
            case "!":
                entity = "Exclamation"
                break
            case "%":
                entity = "Percent"
                break
            case "&>":
                entity = "AmpersandGreaterThan"
                break
            case "-":
                entity = "Hyphen"
                break
            case "--":
                entity = "HyphenHyphen"
                break
            case "*":
                entity = "Asterisk"
                break
            case "+'":
                entity = "PlusApostrophe"
                break
            case "+":
                entity = "Plus"
                break
            case "++":
                entity = "PlusPlus"
                break
            case ",":
                entity = "Comma"
                break
            case "-'":
                entity = "HyphenApostrophe"
                break
            case "/":
                entity = "ForwardSlash"
                break
            case "<<<":
                entity = "LessThanLessThanLessThan"
                break
            case "='":
                entity = "EqualsApostrophe"
                break
            case ">|":
                entity = "GreaterThanPipe"
                break
            case "|&":
                entity = "PipeAmpersand"
                break
            case "cond_op_=~":
                entity = "CondOpEqualsTilde"
                break
            case "-=_arithmetic":
                entity = "HyphenEqualsArithmetic"
                break
            case "+=_arithmetic":
                entity = "PlusEqualsArithmetic"
                break
            case "Parameter_expansion_operator_'-'":
                entity = "ParameterExpansionOperatorApostropheHyphenApostrophe"
                break
            case "Parameter_expansion_operator_'+'":
                entity = "ParameterExpansionOperatorApostrophePlusApostrophe"
                break
            case "Parameter_expansion_operator_'*'":
                entity = "ParameterExpansionOperatorApostropheAsteriskApostrophe"
                break
            case "Parameter_expansion_operator_'!'":
                entity = "ParameterExpansionOperatorApostropheExclamationApostrophe"
                break
            case "Parameter_expansion_operator_'?'":
                entity = "ParameterExpansionOperatorApostropheQuestionApostrophe"
                break
            case "arithmetic_base_char_(#)":
                entity = "ArithmeticBaseCharLeftParenthesesHashRightParentheses"
                break
        }
        entity = entity.replace("?", "")
        entity = entity.replace("(", "")
        entity = entity.replace(")", "")
        entity = entity.replace("#", "")
        entity = handleBreaker(".", entity)
        entity = handleBreaker("_", entity)
        entity = handleBreaker("-", entity)
        entity = entity.substring(0, 1).toUpperCase() + entity.substring(1)
        return entity + "Artifact"
    }

    static String toValidAttribute(String attribute) {
        //ex. attributeName
        attribute = handleBreaker(".", attribute)
        attribute = handleBreaker("_", attribute)
        attribute = attribute.substring(0, 1).toLowerCase() + attribute.substring(1)
        if (literalAttributes.contains(attribute)) {
            return attribute
        }
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
        if (relation.isNumber()) {
            switch (relation) {
                case "1":
                    relation = "one"
                    break
                case "2":
                    relation = "two"
                    break
                default:
                    throw new UnsupportedOperationException("Unsupported relation: $relation")
            }
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
