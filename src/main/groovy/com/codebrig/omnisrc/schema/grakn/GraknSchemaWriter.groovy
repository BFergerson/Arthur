package com.codebrig.omnisrc.schema.grakn

import com.codebrig.omnisrc.observe.ObservedLanguage
import com.codebrig.omnisrc.observe.ObservedLanguages
import com.codebrig.omnisrc.schema.SchemaWriter
import com.codebrig.omnisrc.schema.SegmentedSchemaConfig
import com.google.common.base.CaseFormat

/**
 * todo: description
 *
 * @version 0.2
 * @since 0.1
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class GraknSchemaWriter implements SchemaWriter {

    private final ObservedLanguage rootLanguage
    private final List<ObservedLanguage> observedLanguages
    private boolean naturalOrdering = true

    GraknSchemaWriter(ObservedLanguage observedLanguage) {
        this.rootLanguage = Objects.requireNonNull(observedLanguage)
        this.observedLanguages = Collections.singletonList(observedLanguage)
    }

    GraknSchemaWriter(ObservedLanguages rootLanguage, ObservedLanguage... observedLanguages) {
        this.rootLanguage = Objects.requireNonNull(rootLanguage)
        this.observedLanguages = Arrays.asList(observedLanguages)
    }

    private void doSemanticRoles(Writer output) {
        println "Writing semantic roles"
        def observedRoles = rootLanguage.getObservedRoles(naturalOrdering)
        if (!observedRoles.isEmpty()) {
            output.append("\n##########---------- Semantic Roles ----------##########\n")
        }
        for (int i = 0; i < observedRoles.size(); i++) {
            def role = observedRoles.get(i)
            output.append(role).append(" sub relationship\n")
            output.append("\trelates IS_").append(role).append(";\n")
            output.append("IS_").append(role).append(" sub role;\n")

            if ((i + 1) < observedRoles.size()) {
                output.append("\n")
            }
        }
    }

    private void doAttributes(Writer output) {
        println "Writing attributes"
        output.append("\n##########---------- Attributes ----------##########\n")
        output.append("token sub attribute datatype string;\n")

        if (rootLanguage.isOmnilingual()) {
            outputAttributes(output, rootLanguage)
        }
        observedLanguages.each { observedLanguage ->
            if (!observedLanguage.isOmnilingual()) {
                outputAttributes(output, observedLanguage)
            }
        }
    }

    private void outputAttributes(Writer output, ObservedLanguage observedLanguage) {
        def observedAttributes = observedLanguage.getObservedAttributes(naturalOrdering)
        if (!observedAttributes.isEmpty()) {
            output.append("\n#####----- " + observedLanguage.language.qualifiedName + " -----#####\n")
        }
        observedAttributes.each {
            def attribute = observedLanguage.getAttribute(it, rootLanguage.isOmnilingual())
            output.append(attribute).append(" sub ").append(observedLanguage.getAttributeExtends(attribute))
                    .append(" datatype string;\n") //todo: dynamic datatype
        }
    }

    private void doStructuralRelationships(Writer output) {
        println "Writing structural relationships"
        output.append("\n##########---------- Structural Relationships ----------##########\n")
        output.append("parent_child_relation sub relationship\n" +
                "\trelates is_parent, relates is_child;\n" +
                "is_parent sub role;\n" +
                "is_child sub role;\n")

        if (rootLanguage.isOmnilingual()) {
            outputStructuralRelationships(output, rootLanguage)
        }
        observedLanguages.each { observedLanguage ->
            if (!observedLanguage.isOmnilingual()) {
                outputStructuralRelationships(output, observedLanguage)
            }
        }
    }

    private void outputStructuralRelationships(Writer output, ObservedLanguage observedLanguage) {
        def observedRelations = observedLanguage.getObservedRelations(naturalOrdering)
        observedRelations.remove("parent") //already defined
        observedRelations.remove("child") //already defined
        if (!observedRelations.isEmpty()) {
            output.append("\n#####----- " + observedLanguage.language.qualifiedName + " -----#####\n")
        }
        for (int i = 0; i < observedRelations.size(); i++) {
            def relation = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, observedRelations.get(i) as String)
            def fullRelation = observedLanguage.getRelation(relation, rootLanguage.isOmnilingual())
            def relationRole = observedLanguage.getRelationRole(relation, rootLanguage.isOmnilingual())
            def isRole = "is_$relationRole"
            def hasRole = "has_$relationRole"
            def subType = observedLanguage.getRelationExtends(fullRelation)

            output.append(fullRelation).append(" sub ").append(subType).append("\n")
            output.append("\trelates ").append(isRole)
            output.append(", relates ").append(hasRole).append(";\n")
            if (subType == "parent_child_relation") {
                output.append(isRole).append(" sub ").append("is_child").append(";\n")
                output.append(hasRole).append(" sub ").append("is_parent").append(";\n")
            } else {
                output.append(isRole).append(" sub ").append("is_$relation").append(";\n")
                output.append(hasRole).append(" sub ").append("has_$relation").append(";\n")
            }

            if ((i + 1) < observedRelations.size()) {
                output.append("\n")
            }
        }
    }

    private void doEntities(Writer output) {
        println "Writing entities"
        output.append("\n##########---------- Entities ----------##########\n")
        output.append("SourceArtifact sub entity\n")
                .append("\thas token;\n")

        if (rootLanguage.isOmnilingual()) {
            outputEntities(output, rootLanguage)
        }
        observedLanguages.each { observedLanguage ->
            if (!observedLanguage.isOmnilingual()) {
                outputEntities(output, observedLanguage)
            }
        }
    }

    private void outputEntities(Writer output, ObservedLanguage observedLanguage) {
        output.append("\n#####----- " + observedLanguage.language.qualifiedName + " -----#####\n")
        output.append(observedLanguage.language.qualifiedName).append("SourceArtifact sub SourceArtifact;\n\n")

        def observedEntities = observedLanguage.getObservedEntities(naturalOrdering)
        for (int i = 0; i < observedEntities.size(); i++) {
            def entity = observedEntities.get(i)
            def fullEntity = observedLanguage.getEntity(entity, rootLanguage.isOmnilingual())
            output.append(fullEntity).append(" sub ").append(observedLanguage.getEntityExtends(fullEntity))

            //has
            if (observedLanguage.attributes.containsKey(entity)) {
                def attrList = observedLanguage.getEntityObservedAttributes(entity, naturalOrdering)
                if (!attrList.isEmpty()) output.append("\n\t# Attributes\n")
                for (int z = 0; z < attrList.size(); z++) {
                    def attribute = observedLanguage.getAttribute(attrList.get(z), rootLanguage.isOmnilingual())
                    output.append("\thas ").append(attribute)

                    if ((z + 1) < attrList.size()) {
                        output.append("\n")
                    }
                }
            }

            //plays (UAST structure)
            if (observedLanguage.relations.containsKey(entity)) {
                def isRelations = observedLanguage.getEntityObservedIsRelations(entity, naturalOrdering)
                def hasRelations = observedLanguage.getEntityObservedHasRelations(entity, naturalOrdering)
                if (!isRelations.isEmpty() || !hasRelations.isEmpty()) output.append("\n\t# Structural\n")
                for (int z = 0; z < isRelations.size(); z++) {
                    output.append("\tplays is_").append(observedLanguage.getRelationRole(
                            isRelations.get(z), rootLanguage.isOmnilingual()))

                    if ((z + 1) < isRelations.size() || !hasRelations.isEmpty()) {
                        output.append("\n")
                    }
                }
                for (int z = 0; z < hasRelations.size(); z++) {
                    output.append("\tplays has_").append(observedLanguage.getRelationRole(
                            hasRelations.get(z), rootLanguage.isOmnilingual()))

                    if ((z + 1) < hasRelations.size()) {
                        output.append("\n")
                    }
                }
            }

            //plays (semantic roles)
            if (observedLanguage.roles.containsKey(entity)) {
                def roleList = observedLanguage.getEntityObservedRoles(entity, naturalOrdering)
                if (!roleList.isEmpty()) {
                    output.append("\n\t# Semantic\n")
                    for (int z = 0; z < roleList.size(); z++) {
                        output.append("\tplays IS_").append(roleList.get(z))

                        if ((z + 1) < roleList.size()) {
                            output.append("\n")
                        }
                    }
                }
            }
            output.append(";\n")

            if ((i + 1) < observedEntities.size()) {
                output.append("\n")
            }
        }
    }

    boolean getNaturalOrdering() {
        return naturalOrdering
    }

    void setNaturalOrdering(boolean naturalOrdering) {
        this.naturalOrdering = naturalOrdering
    }

    @Override
    void storeSegmentedSchemaDefinition(SegmentedSchemaConfig segmentConfig) {
        println "todo: this" //todo: this
    }

    @Override
    String getFullSchemaDefinition() {
        def sb = new StringWriter()
        sb.append("define\n")
        doAttributes(sb)
        doEntities(sb)
        doStructuralRelationships(sb)
        doSemanticRoles(sb)
        return sb.toString()
    }
}
