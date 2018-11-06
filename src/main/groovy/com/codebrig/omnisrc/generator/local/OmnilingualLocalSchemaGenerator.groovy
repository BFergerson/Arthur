package com.codebrig.omnisrc.generator.local

import com.codebrig.omnisrc.SourceLanguage
import com.codebrig.omnisrc.generator.SchemaGenerator
import com.codebrig.omnisrc.observations.ObservedLanguage
import com.codebrig.omnisrc.observations.OmniObservedLanguage
import com.codebrig.omnisrc.schema.grakn.GraknSchemaWriter

import java.util.concurrent.TimeUnit

/**
 * todo: description
 *
 * @version 0.2
 * @since 0.2
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class OmnilingualLocalSchemaGenerator extends SchemaGenerator {

    static void main(String[] args) {
        def inputDirectory = new File(args[0])
        long startTime = System.currentTimeMillis()
        def schemaGenerator = new SchemaGenerator()
        def observedLanguages = new ArrayList<ObservedLanguage>()
        SourceLanguage.values().each {
            if (it != SourceLanguage.OmniSRC) {
                observedLanguages.add(schemaGenerator.observeLanguage(it, inputDirectory))
            }
        }

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

        def schemaWriter = new GraknSchemaWriter(omniLanguage, observedLanguages.toArray(new ObservedLanguage[0]))
        def outputFile = new File("src/main/resources/schema/omnilingual/OmniSRC_" + SourceLanguage.OmniSRC.qualifiedName + "_Schema.gql")
        if (outputFile.exists()) outputFile.delete()
        outputFile.createNewFile()
        outputFile.write(schemaWriter.getSchemaDefinition())
        println "Completed in: " + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s"
    }
}
