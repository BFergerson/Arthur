package com.codebrig.omnisrc.generator.local

import com.codebrig.omnisrc.SourceLanguage
import com.codebrig.omnisrc.generator.SchemaGenerator

import java.util.concurrent.TimeUnit

/**
 * todo: description
 *
 * @version 0.2
 * @since 0.1
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class UnilingualLocalSchemaGenerator extends SchemaGenerator {

    static void main(String[] args) {
        def language = SourceLanguage.getSourceLanguageByName(args[0])
        def inputDirectory = args[1] as File
        long startTime = System.currentTimeMillis()
        new SchemaGenerator().generateUnilingualSchema(language, inputDirectory,
                new File("src/main/resources/schema/unilingual/" + language.key, language.getSchemaDefinitionName() + ".gql"))
        println "Completed in: " + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s"
    }
}
