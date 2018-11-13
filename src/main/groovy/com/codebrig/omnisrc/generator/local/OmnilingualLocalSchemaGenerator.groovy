package com.codebrig.omnisrc.generator.local

import com.codebrig.omnisrc.SourceLanguage
import com.codebrig.omnisrc.generator.SchemaGenerator
import com.codebrig.omnisrc.observe.ObservedLanguage
import com.codebrig.omnisrc.observe.ObservedLanguages
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

        def omniLanguage = ObservedLanguages.mergeLanguages(observedLanguages)
        def schemaWriter = new GraknSchemaWriter(omniLanguage, observedLanguages.toArray(new ObservedLanguage[0]))
        def outputFile = new File("src/main/resources/schema/omnilingual/OmniSRC_" + SourceLanguage.OmniSRC.qualifiedName + "_Schema.gql")
        if (outputFile.exists()) outputFile.delete()
        outputFile.createNewFile()
        outputFile.write(schemaWriter.getSchemaDefinition())
        println "Completed in: " + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s"
    }
}
