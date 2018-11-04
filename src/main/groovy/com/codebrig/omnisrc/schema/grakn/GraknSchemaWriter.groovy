package com.codebrig.omnisrc.schema.grakn

import com.codebrig.omnisrc.observations.ObservedLanguage
import com.codebrig.omnisrc.observations.OmniObservedLanguage
import com.google.common.base.CaseFormat

/**
 * @author github.com/BFergerson
 */
class GraknSchemaWriter {

    private final ObservedLanguage rootLanguage
    private final List<ObservedLanguage> observedLanguages
    private boolean naturalOrdering = true

    GraknSchemaWriter(ObservedLanguage observedLanguage) {
        this.observedLanguages = Collections.singletonList(Objects.requireNonNull(observedLanguage))
        this.rootLanguage = observedLanguage
    }

    GraknSchemaWriter(OmniObservedLanguage rootLanguage, ObservedLanguage... observedLanguages) {
        this.rootLanguage = rootLanguage
        this.observedLanguages = Arrays.asList(observedLanguages)
    }

    private void doSemanticRoles(StringBuilder sb) {
        println "Writing semantic roles"
        sb.append("\n##########---------- Semantic Roles ----------##########\n")

        def observedRoles = rootLanguage.getObservedRoles(naturalOrdering)
        for (int i = 0; i < observedRoles.size(); i++) {
            def role = observedRoles.get(i)
            sb.append(role).append(" sub relationship\n")
            sb.append("\trelates IS_").append(role).append(";\n")
            sb.append("IS_").append(role).append(" sub role;\n")

            if ((i + 1) < observedRoles.size()) {
                sb.append("\n")
            }
        }
    }

    private void doAttributes(StringBuilder sb) {
        println "Writing attributes"
        sb.append("\n##########---------- Attributes ----------##########\n")

        //todo: smarter and dynamic
        sb.append("token sub attribute datatype string;\n")

        if (rootLanguage.isOmnilingual()) {
            outputAttributes(sb, rootLanguage)
        }
        observedLanguages.each { observedLanguage ->
            outputAttributes(sb, observedLanguage)
        }
    }

    private void outputAttributes(StringBuilder sb, ObservedLanguage observedLanguage) {
        sb.append("\n#####----- " + observedLanguage.language.qualifiedName + " -----#####\n")
        observedLanguage.getObservedAttributes(naturalOrdering).each {
            def attribute = observedLanguage.getAttribute(it, rootLanguage.isOmnilingual())
            sb.append(attribute).append(" sub ").append(observedLanguage.getAttributeExtends(attribute))
                    .append(" datatype string;\n") //todo: dynamic datatype
        }
    }

    private void doStructuralRelationships(StringBuilder sb) {
        println "Writing structural relationships"
        sb.append("\n##########---------- Structural Relationships ----------##########\n")

        //todo: smarter and dynamic
        sb.append("parent_child_relation sub relationship\n" +
                "\trelates is_parent, relates is_child;\n" +
                "is_parent sub role;\n" +
                "is_child sub role;\n")

        if (rootLanguage.isOmnilingual()) {
            outputStructuralRelationships(sb, rootLanguage)
        }
        observedLanguages.each { observedLanguage ->
            outputStructuralRelationships(sb, observedLanguage)
        }
    }

    private void outputStructuralRelationships(StringBuilder sb, ObservedLanguage observedLanguage) {
        sb.append("\n#####----- " + observedLanguage.language.qualifiedName + " -----#####\n")
        def observedRelations = observedLanguage.getObservedRelations(naturalOrdering)
        for (int i = 0; i < observedRelations.size(); i++) {
            def relation = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, observedRelations.get(i) as String)
            def fullRelation = observedLanguage.getRelation(relation, rootLanguage.isOmnilingual())
            def isRole = "is_$fullRelation"
            def hasRole = "has_$fullRelation"
            def subType = observedLanguage.getRelationExtends(fullRelation)

            sb.append(fullRelation).append(" sub ").append(subType).append("\n")
            sb.append("\trelates ").append(isRole)
            sb.append(", relates ").append(hasRole).append(";\n")
            if (subType == "parent_child_relation") {
                sb.append(isRole).append(" sub ").append("is_child").append(";\n")
                sb.append(hasRole).append(" sub ").append("is_parent").append(";\n")
            } else {
                sb.append(isRole).append(" sub ").append("is_$relation").append(";\n")
                sb.append(hasRole).append(" sub ").append("has_$relation").append(";\n")
            }

            if ((i + 1) < observedRelations.size()) {
                sb.append("\n")
            }
        }
    }

    private void doEntities(StringBuilder sb) {
        println "Writing globalEntities"
        sb.append("\n##########---------- Entities ----------##########\n")
        sb.append("SourceArtifact sub entity\n")
                .append("\thas token;\n")

        if (rootLanguage.isOmnilingual()) {
            outputEntities(sb, rootLanguage)
        }
        observedLanguages.each { observedLanguage ->
            outputEntities(sb, observedLanguage)
        }
    }

    private void outputEntities(StringBuilder sb, ObservedLanguage observedLanguage) {
        sb.append("\n#####----- " + observedLanguage.language.qualifiedName + " -----#####\n")
        sb.append(observedLanguage.language.qualifiedName).append("SourceArtifact sub SourceArtifact;\n\n")

        def observedEntities = observedLanguage.getObservedEntities(naturalOrdering)
        for (int i = 0; i < observedEntities.size(); i++) {
            def entity = observedEntities.get(i)
            def fullEntity = observedLanguage.getEntity(entity, rootLanguage.isOmnilingual())
            sb.append(fullEntity).append(" sub ").append(observedLanguage.getEntityExtends(fullEntity))

            //has
            if (observedLanguage.attributes.containsKey(entity)) {
                def attrList = observedLanguage.getEntityObservedAttributes(entity, naturalOrdering)
                if (!attrList.isEmpty()) sb.append("\n\t# Attributes\n")
                for (int z = 0; z < attrList.size(); z++) {
                    def attribute = observedLanguage.getAttribute(attrList.get(z), rootLanguage.isOmnilingual())
                    sb.append("\thas ").append(attribute)

                    if ((z + 1) < attrList.size()) {
                        sb.append("\n")
                    }
                }
            }

            //plays (UAST structure)
            if (observedLanguage.relations.containsKey(entity)) {
                def isRelations = observedLanguage.getEntityObservedIsRelations(entity, naturalOrdering)
                def hasRelations = observedLanguage.getEntityObservedHasRelations(entity, naturalOrdering)
                if (!isRelations.isEmpty() || !hasRelations.isEmpty()) sb.append("\n\t# Structural\n")
                for (int z = 0; z < isRelations.size(); z++) {
                    sb.append("\tplays is_").append(observedLanguage.getRelation(
                            isRelations.get(z), rootLanguage.isOmnilingual()))

                    if ((z + 1) < isRelations.size() || !hasRelations.isEmpty()) {
                        sb.append("\n")
                    }
                }
                for (int z = 0; z < hasRelations.size(); z++) {
                    sb.append("\tplays has_").append(observedLanguage.getRelation(
                            hasRelations.get(z), rootLanguage.isOmnilingual()))

                    if ((z + 1) < hasRelations.size()) {
                        sb.append("\n")
                    }
                }
            }

            //plays (semantic roles)
            if (observedLanguage.roles.containsKey(entity)) {
                def roleList = observedLanguage.getEntityObservedRoles(entity, naturalOrdering)
                if (!roleList.isEmpty()) {
                    sb.append("\n\t# Semantic\n")
                    for (int z = 0; z < roleList.size(); z++) {
                        sb.append("\tplays IS_").append(roleList.get(z))

                        if ((z + 1) < roleList.size()) {
                            sb.append("\n")
                        }
                    }
                }
            }
            sb.append(";\n")

            if ((i + 1) < observedEntities.size()) {
                sb.append("\n")
            }
        }
    }

    boolean getNaturalOrdering() {
        return naturalOrdering
    }

    void setNaturalOrdering(boolean naturalOrdering) {
        this.naturalOrdering = naturalOrdering
    }

    String getSchemaDefinition() {
        def sb = new StringBuilder()
        sb.append("define\n")
        doAttributes(sb)
        doEntities(sb)
        doStructuralRelationships(sb)
        doSemanticRoles(sb)
        return sb.toString()
    }
}
