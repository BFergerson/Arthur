package com.codebrig.omnisrc.generator.github

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
class UnilingualGithubSchemaGenerator extends SchemaGenerator {

    static void main(String[] args) {
        def language = SourceLanguage.valueOf(args[0])
        def parseProjectsCount = args[1] as int
        long startTime = System.currentTimeMillis()
        new SchemaGenerator().generateUnilingualSchema(language, parseProjectsCount,
                new File("src/main/resources/schema/unilingual/" + language.key, language.getSchemaDefinitionName() + ".gql"))
        println "Completed in: " + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s"
    }
}
