//package com.codebrig.arthur.schema.grakn
//
//import com.codebrig.arthur.SourceLanguage
//import com.codebrig.arthur.observe.ObservationConfig
//import com.codebrig.arthur.observe.ObservedLanguage
//import com.codebrig.arthur.observe.ObservedLanguages
//import com.codebrig.arthur.schema.SchemaSegment
//import com.codebrig.arthur.schema.SegmentedSchemaConfig
//import graql.lang.Graql
//import graql.lang.pattern.constraint.TypeConstraint
//import graql.lang.query.GraqlDefine
//
//class GraknSchemaReader {
//
//    static void main(String[] args) {
//        def goLanguage = readGraqlDefinitions(SourceLanguage.Go)
//        def javaLanguage = readGraqlDefinitions(SourceLanguage.Java)
//        def javascriptLanguage = readGraqlDefinitions(SourceLanguage.Javascript)
//        def phpLanguage = readGraqlDefinitions(SourceLanguage.Php)
//        def pythonLanguage = readGraqlDefinitions(SourceLanguage.Python)
//        def rubyLanguage = readGraqlDefinitions(SourceLanguage.Ruby)
//        def cSharpLanguage = readGraqlDefinitions(SourceLanguage.CSharp)
//        def bashLanguage = readGraqlDefinitions(SourceLanguage.Bash)
//        def cppLanguage = readGraqlDefinitions(SourceLanguage.CPlusPlus)
//        def omniLanguage = ObservedLanguages.mergeLanguages(goLanguage, javaLanguage, javascriptLanguage, phpLanguage,
//                pythonLanguage, rubyLanguage, cSharpLanguage, bashLanguage, cppLanguage)
//        def schemaWriter = new GraknSchemaWriter(omniLanguage, goLanguage, javaLanguage, javascriptLanguage, phpLanguage,
//                pythonLanguage, rubyLanguage, cSharpLanguage, bashLanguage, cppLanguage)
//        schemaWriter.storeSegmentedSchemaDefinition(new SegmentedSchemaConfig()
//                .withFileSegment(new File("src/main/resources/schema/omnilingual/",
//                        "Arthur_" + SourceLanguage.Omnilingual.qualifiedName + "_Base_Structure.gql"), ObservationConfig.baseStructure().asArray())
//                .withFileSegment(new File("src/main/resources/schema/omnilingual/",
//                        "Arthur_" + SourceLanguage.Omnilingual.qualifiedName + "_Semantic_Roles.gql"), SchemaSegment.SEMANTIC_ROLES))
//    }
//
//    static ObservedLanguage readGraqlDefinitions(SourceLanguage language) {
//        return readGraqlDefinitions(language, language.baseStructureSchemaDefinition, language.semanticRolesSchemaDefinition)
//    }
//
//    static ObservedLanguage readGraqlDefinitions(SourceLanguage language,
//                                                 String structureDefinition, String roleDefinition) {
//        def observedLanguage = new ObservedLanguage(language)
//        (Graql.parseQuery(structureDefinition) as GraqlDefine).variables().each {
//            def name = ""
//            def type = ""
//            def dataType = ""
//            List<String> hasAttributes = []
//            List<String> playsRelations = []
//
//            it.properties().each {
//                switch (it) {
//                    case TypeConstraint:
//                        name = (it as TypeConstraint).name()
//                        break
//                    case TypeConstraint.Sub:
//                        type = (it as TypeConstraint.Sub).type().type
//                        break
//                    case TypeConstraint.ValueType:
//                        dataType = (it as TypeConstraint.ValueType).valueType().name()
//                        break
//                    case TypeConstraint.Owns:
//                        hasAttributes << (it as TypeConstraint.Owns).attributeType().type.get()
//                        break
//                    case TypeConstraint.Plays:
//                        def role = (it as TypeConstraint.Plays).role().type.get()
//                        if (role.endsWith("role")) {
//                            playsRelations << role[0..-5] + "relation"
//                        } else {
//                            playsRelations << role
//                        }
//                        break
//                }
//            }
//
//            switch (type) {
//                case "attribute":
//                case "relation":
//                case "parent_child_relation":
//                    break
//                default:
//                    if (!(name in ["SourceArtifact", language.qualifiedName + "SourceArtifact"])) {
//                        name = name[0..-9]
//                        if (language.structureNaming.isNamedNodeType(name)) {
//                            hasAttributes.remove("name")
//                        }
//                        observedLanguage.observeAttributes(name, hasAttributes.collectEntries {
//                            def attribute = it as String
//                            if (attribute.endsWith("Attribute")) {
//                                [(attribute[0..-10]): ""]
//                            } else {
//                                [(attribute): ""]
//                            }
//                        })
//
//                        observedLanguage.observeRelations(name, playsRelations)
//                    }
//            }
//        }
//        (Graql.parse(roleDefinition) as GraqlDefine).statements().each {
//            def name = ""
//            List<String> playsRoles = []
//
//            it.properties().each {
//                switch (it) {
//                    case TypeConstraint:
//                        name = (it as TypeConstraint).name()
//                        if (name.endsWith("Artifact")) {
//                            name = name[0..-9]
//                        }
//                        break
//                    case TypeConstraint.Plays:
//                        playsRoles << (it as TypeConstraint.Plays).role().type.get().substring(3)
//                        break
//                }
//            }
//
//            observedLanguage.observeRoles(name, playsRoles)
//        }
//
//        def schemaWriter = new GraknSchemaWriter(observedLanguage)
//        assert schemaWriter.getSegmentedSchemaDefinition(ObservationConfig.baseStructure().asArray()) == language.baseStructureSchemaDefinition
//        assert schemaWriter.getSegmentedSchemaDefinition(SchemaSegment.SEMANTIC_ROLES) == language.semanticRolesSchemaDefinition
//        return observedLanguage
//    }
//}
