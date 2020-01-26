package com.codebrig.arthur.schema.grakn

import com.codebrig.arthur.observe.ObservedLanguage
import com.codebrig.arthur.observe.ObservedLanguages
import com.codebrig.arthur.observe.structure.StructureLiteral
import com.codebrig.arthur.schema.SchemaSegment
import com.codebrig.arthur.schema.SchemaWriter
import com.google.common.base.CaseFormat
import groovy.util.logging.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import static com.codebrig.arthur.schema.SchemaSegment.*

/**
 * Used to create Grakn compatible Arthur schemas
 *
 * @version 0.4
 * @since 0.1
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
@Slf4j
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

    private void writeSemanticRoles(Writer output) {
        log.info "Writing semantic roles"
        def observedRoles = rootLanguage.getObservedSemanticRoles(naturalOrdering)

        if (!observedRoles.isEmpty()) {
            output.append("\n##########---------- Semantic Roles ----------##########\n")
        }
        for (int i = 0; i < observedRoles.size(); i++) {
            def role = observedRoles.get(i)
            output.append(role).append(" sub relation,\n")
            output.append("\trelates IS_").append(role).append(";\n")

            if ((i + 1) < observedRoles.size()) {
                output.append("\n")
            }
        }
    }

    private void writeAttributes(Writer output) {
        log.info "Writing attributes"
        output.append("\n##########---------- Attributes ----------##########\n")
        StructureLiteral.allLiteralAttributes.each {
            output.append(it.key).append(" sub attribute, datatype ").append(it.value).append(";\n")
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
                    .append(", datatype ").append(GraknAttributeDatatype.getType(attribute))
                    .append(";\n")
        }
    }

    private void writeStructuralRelationships(Writer output) {
        log.info "Writing structural relationships"
        output.append("\n##########---------- Structural Relationships ----------##########\n")
        output.append("parent_child_relation sub relation,\n" +
                "\trelates is_parent, relates is_child;\n")

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

            def childAs = "is_child"
            def parentAs = "is_parent"
            if (subType != "parent_child_relation") {
                def subRole = observedLanguage.getRelationRole(relation, false)
                childAs = "is_$subRole"
                parentAs = "has_$subRole"
            }
            output.append(fullRelation).append(" sub ").append(subType).append(",\n")
            output.append("\trelates ").append(isRole).append(" as ").append(childAs)
            output.append(", relates ").append(hasRole).append(" as ").append(parentAs).append(";\n")

            if ((i + 1) < observedRelations.size()) {
                output.append("\n")
            }
        }
    }

    private void writeEntities(Writer output, boolean includeAttributes, boolean includeStructure,
                               boolean includeSemantics) {
        log.info "Writing entities"
        output.append("\n##########---------- Entities ----------##########\n")
        if (includeAttributes) {
            output.append("SourceArtifact sub entity,\n")
                    .append("\thas token,\n")
                    .append("\tplays is_child,\n")
                    .append("\tplays is_parent;\n")
        }

        observedLanguages.each { observedLanguage ->
            if (!observedLanguage.isOmnilingual()) {
                outputEntities(output, observedLanguage, includeAttributes, includeStructure, includeSemantics)
            }
        }
    }

    private void outputEntities(Writer output, ObservedLanguage observedLanguage,
                                boolean includeAttributes, boolean includeStructure, boolean includeSemantics) {
        output.append("\n#####----- " + observedLanguage.language.qualifiedName + " -----#####\n")
        output.append(observedLanguage.language.qualifiedName).append("SourceArtifact sub SourceArtifact;\n")

        def observedEntities = observedLanguage.getObservedEntities(naturalOrdering)
        if (!observedEntities.isEmpty()) {
            output.append("\n")
        }
        for (int i = 0; i < observedEntities.size(); i++) {
            def entity = observedEntities.get(i)
            def fullEntity = observedLanguage.getEntity(entity, rootLanguage.isOmnilingual())
            output.append(fullEntity).append(" sub ").append(observedLanguage.getEntityExtends(fullEntity))

            //has
            if (includeAttributes && observedLanguage.attributes.containsKey(entity)) {
                def attrList = observedLanguage.getEntityObservedAttributes(entity, naturalOrdering)
                def entityWithoutArtifact = entity.replace("Artifact", "")
                if (observedLanguage.language.structureNaming.isNamedNodeType(entityWithoutArtifact)) {
                    attrList.add("name")
                }

                if (!attrList.isEmpty()) output.append(",\n\t# Attributes\n")
                for (int z = 0; z < attrList.size(); z++) {
                    def attribute = observedLanguage.getAttribute(attrList.get(z), rootLanguage.isOmnilingual())
                    output.append("\thas ").append(attribute)

                    if ((z + 1) < attrList.size()) {
                        output.append(",\n")
                    }
                }
            }

            //plays (UAST structure)
            if (includeStructure && observedLanguage.relations.containsKey(entity)) {
                def isRelations = observedLanguage.getEntityObservedIsRelations(entity, naturalOrdering)
                def hasRelations = observedLanguage.getEntityObservedHasRelations(entity, naturalOrdering)
                if (!isRelations.isEmpty() || !hasRelations.isEmpty()) output.append(",\n\t# Structural\n")
                for (int z = 0; z < isRelations.size(); z++) {
                    output.append("\tplays is_").append(observedLanguage.getRelationRole(
                            isRelations.get(z), rootLanguage.isOmnilingual()))

                    if ((z + 1) < isRelations.size() || !hasRelations.isEmpty()) {
                        output.append(",\n")
                    }
                }
                for (int z = 0; z < hasRelations.size(); z++) {
                    output.append("\tplays has_").append(observedLanguage.getRelationRole(
                            hasRelations.get(z), rootLanguage.isOmnilingual()))

                    if ((z + 1) < hasRelations.size()) {
                        output.append(",\n")
                    }
                }
            }

            //plays (semantic roles)
            if (includeSemantics && observedLanguage.roles.containsKey(entity)) {
                def roleList = observedLanguage.getEntityObservedRoles(entity, naturalOrdering)
                if (!roleList.isEmpty()) {
                    output.append(",\n\t# Semantic\n")
                    for (int z = 0; z < roleList.size(); z++) {
                        output.append("\tplays IS_").append(roleList.get(z))

                        if ((z + 1) < roleList.size()) {
                            output.append(",\n")
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
                || SEMANTIC_ROLES in segments) {
            def includeAttributes = ATTRIBUTES in segments
            def includeStructure = RELATIONSHIPS in segments
            def includeSemantics = (SEMANTIC_ROLES in segments)
            writeEntities(sb, includeAttributes, includeStructure, includeSemantics)
        }
        if (RELATIONSHIPS in segments) {
            writeStructuralRelationships(sb)
        }
        if (SEMANTIC_ROLES in segments) {
            writeSemanticRoles(sb)
        }
        return sb.toString()
    }

    @Override
    String getFullSchemaDefinition() {
        def sb = new StringWriter()
        sb.append("define\n")
        writeAttributes(sb)
        writeEntities(sb, true, true, true)
        writeStructuralRelationships(sb)
        writeSemanticRoles(sb)
        return sb.toString()
    }
}
