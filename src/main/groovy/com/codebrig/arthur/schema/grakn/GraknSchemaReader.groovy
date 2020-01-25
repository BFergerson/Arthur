package com.codebrig.arthur.schema.grakn

import com.codebrig.arthur.SourceLanguage
import com.codebrig.arthur.observe.ObservedLanguage
import graql.lang.Graql
import graql.lang.property.*
import graql.lang.query.GraqlDefine

class GraknSchemaReader {

    static void main(String[] args) {
        SourceLanguage.values().each {
            if (it != SourceLanguage.Omnilingual && it != SourceLanguage.Bash) {
                readTest(it)
            }
        }
    }

    static void readTest(SourceLanguage language) {
        def defineQuery = Graql.parse(language.baseStructureSchemaDefinition) as GraqlDefine

        def observedLanguage = new ObservedLanguage(language)
        defineQuery.statements().each {
            def name = ""
            def type = ""
            def dataType = ""
            def hasAttributes = []
            def playsRelations = []

            it.properties().each {
                switch (it) {
                    case TypeProperty:
                        name = (it as TypeProperty).name()
                        break
                    case SubProperty:
                        type = (it as SubProperty).type().type.get()
                        break
                    case DataTypeProperty:
                        dataType = (it as DataTypeProperty).dataType().name()
                        break
                    case HasAttributeTypeProperty:
                        hasAttributes << (it as HasAttributeTypeProperty).attributeType().type.get()
                        break
                    case PlaysProperty:
                        def role = (it as PlaysProperty).role().type.get()
                        if (role.endsWith("role")) {
                            playsRelations << role[0..-5] + "relation"
                        } else {
                            playsRelations << role
                        }
                        break
                }
            }

            switch (type) {
                case "attribute":
                case "relation":
                case "parent_child_relation":
                    break
                default:
                    if (!(name in ["SourceArtifact", language.qualifiedName + "SourceArtifact"])) {
                        name = name[0..-9]
                        if (language.structureNaming.isNamedNodeType(name)) {
                            hasAttributes.remove("name")
                        }
                        observedLanguage.observeAttributes(name, hasAttributes.collectEntries {
                            def attribute = it as String
                            if (attribute.endsWith("Attribute")) {
                                [(attribute[0..-10]): ""]
                            } else {
                                [(attribute): ""]
                            }
                        })

                        observedLanguage.observeRelations(name, playsRelations)
                    }
            }
        }

        def schemaWriter = new GraknSchemaWriter(observedLanguage)
        assert schemaWriter.fullSchemaDefinition == language.baseStructureSchemaDefinition
    }
}
