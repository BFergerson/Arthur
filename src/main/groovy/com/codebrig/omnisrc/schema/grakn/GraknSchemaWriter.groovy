package com.codebrig.omnisrc.schema.grakn

import com.codebrig.omnisrc.observe.ObservedLanguage
import com.codebrig.omnisrc.observe.ObservedLanguages
import com.codebrig.omnisrc.observe.structure.StructureLiteral
import com.codebrig.omnisrc.schema.SchemaSegment
import com.codebrig.omnisrc.schema.SchemaWriter
import com.google.common.base.CaseFormat

import static com.codebrig.omnisrc.schema.SchemaSegment.*

/**
 * Used to create Grakn compatible OmniSRC schemas
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

    private void writeSemanticRoles(Writer output, boolean individualRoles, boolean actualRoles, boolean possibleRoles) {
        println "Writing semantic roles"
        def observedRoles
        if (individualRoles && actualRoles && possibleRoles) {
            observedRoles = rootLanguage.getObservedRoles(naturalOrdering)
        } else {
            observedRoles = new ArrayList<String>()
            if (individualRoles) {
                observedRoles.addAll(rootLanguage.getObservedRoles(naturalOrdering,
                        true, false, false))
            }
            if (actualRoles) {
                //get from observed languages directly; root language won't know
                observedLanguages.each {
                    observedRoles.addAll(it.getObservedRoles(naturalOrdering,
                            false, true, false))
                }
            }
            if (possibleRoles) {
                observedRoles.addAll(rootLanguage.getObservedRoles(naturalOrdering,
                        false, false, true))
            }
        }

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

    private void writeAttributes(Writer output) {
        println "Writing attributes"
        output.append("\n##########---------- Attributes ----------##########\n")
        output.append("token sub attribute datatype string;\n")
        StructureLiteral.allLiteralAttributes.each {
            output.append(it.key).append(" sub attribute datatype ").append(it.value).append(";\n")
        }

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
        observedAttributes.removeIf({
            StructureLiteral.allLiteralAttributes.keySet().contains(it.replace("Attribute", ""))
        })
        if (!observedAttributes.isEmpty()) {
            output.append("\n#####----- " + observedLanguage.language.qualifiedName + " -----#####\n")
        }
        observedAttributes.each {
            def attribute = observedLanguage.getAttribute(it, rootLanguage.isOmnilingual())
            output.append(attribute).append(" sub ").append(observedLanguage.getAttributeExtends(attribute))
                    .append(" datatype ").append(GraknAttributeDatatype.getType(attribute))
                    .append(";\n")
        }
    }

    private void writeStructuralRelationships(Writer output) {
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

    private void writeEntities(Writer output, boolean includeAttributes, boolean includeStructure,
                               boolean individualRoles, boolean actualRoles, boolean possibleRoles) {
        println "Writing entities"
        output.append("\n##########---------- Entities ----------##########\n")
        if (includeAttributes) {
            output.append("SourceArtifact sub entity\n")
                    .append("\thas token;\n")
        }

        if (rootLanguage.isOmnilingual()) {
            outputEntities(output, rootLanguage, includeAttributes, includeStructure,
                    individualRoles, actualRoles, possibleRoles)
        }
        observedLanguages.each { observedLanguage ->
            if (!observedLanguage.isOmnilingual()) {
                outputEntities(output, observedLanguage, includeAttributes, includeStructure,
                        individualRoles, actualRoles, possibleRoles)
            }
        }
    }

    private void outputEntities(Writer output, ObservedLanguage observedLanguage,
                                boolean includeAttributes, boolean includeStructure,
                                boolean individualRoles, boolean actualRoles, boolean possibleRoles) {
        output.append("\n#####----- " + observedLanguage.language.qualifiedName + " -----#####\n")
        output.append(observedLanguage.language.qualifiedName).append("SourceArtifact sub SourceArtifact;\n\n")

        def observedEntities = observedLanguage.getObservedEntities(naturalOrdering)
        for (int i = 0; i < observedEntities.size(); i++) {
            def entity = observedEntities.get(i)
            def fullEntity = observedLanguage.getEntity(entity, rootLanguage.isOmnilingual())
            output.append(fullEntity).append(" sub ").append(observedLanguage.getEntityExtends(fullEntity))

            //has
            if (includeAttributes && observedLanguage.attributes.containsKey(entity)) {
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
            if (includeStructure && observedLanguage.relations.containsKey(entity)) {
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
                def roleList
                if (individualRoles && actualRoles && possibleRoles) {
                    roleList = observedLanguage.getEntityObservedRoles(entity, naturalOrdering)
                } else {
                    roleList = new ArrayList<String>()
                    if (individualRoles) {
                        roleList.addAll(observedLanguage.getEntityObservedRoles(entity, naturalOrdering,
                                true, false, false))
                    }
                    if (actualRoles) {
                        roleList.addAll(observedLanguage.getEntityObservedRoles(entity, naturalOrdering,
                                false, true, false))
                    }
                    if (possibleRoles) {
                        roleList.addAll(observedLanguage.getEntityObservedRoles(entity, naturalOrdering,
                                false, false, true))
                    }
                }

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
    String getSegmentedSchemaDefinition(SchemaSegment... segments) {
        def sb = new StringWriter()
        sb.append("define\n")
        if (ATTRIBUTES in segments) {
            writeAttributes(sb)
        }
        if (ENTITIES in segments
                || INDIVIDUAL_SEMANTIC_ROLES in segments
                || ACTUAL_SEMANTIC_ROLES in segments
                || POSSIBLE_SEMANTIC_ROLES in segments) {
            def individualRoles = (INDIVIDUAL_SEMANTIC_ROLES in segments)
            def actualRoles = (ACTUAL_SEMANTIC_ROLES in segments)
            def possibleRoles = (POSSIBLE_SEMANTIC_ROLES in segments)
            def includeAttributes = ATTRIBUTES in segments
            def includeStructure = RELATIONSHIPS in segments
            writeEntities(sb, includeAttributes, includeStructure, individualRoles, actualRoles, possibleRoles)
        }
        if (RELATIONSHIPS in segments) {
            writeStructuralRelationships(sb)
        }
        if (INDIVIDUAL_SEMANTIC_ROLES in segments
                || ACTUAL_SEMANTIC_ROLES in segments
                || POSSIBLE_SEMANTIC_ROLES in segments) {
            def individualRoles = (INDIVIDUAL_SEMANTIC_ROLES in segments)
            def actualRoles = (ACTUAL_SEMANTIC_ROLES in segments)
            def possibleRoles = (POSSIBLE_SEMANTIC_ROLES in segments)
            writeSemanticRoles(sb, individualRoles, actualRoles, possibleRoles)
        }
        return sb.toString()
    }

    @Override
    String getFullSchemaDefinition() {
        def sb = new StringWriter()
        sb.append("define\n")
        writeAttributes(sb)
        writeEntities(sb, true, true, true, true, true)
        writeStructuralRelationships(sb)
        writeSemanticRoles(sb, true, true, true)
        return sb.toString()
    }
}
