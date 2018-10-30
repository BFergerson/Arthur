package com.codebrig.omnisrc.generators

import com.codebrig.omnisrc.SourceLanguage

import java.util.concurrent.TimeUnit

/**
 * @author github.com/BFergerson
 */
class GoSchemaGenerator extends OmniSchemaGenerator {

    public static final SourceLanguage language = SourceLanguage.Go
    public static final int PARSE_PROJECTS = 3

    static void main(String[] args) {
        long startTime = System.currentTimeMillis()
        generateUnilingualSchema(language, PARSE_PROJECTS,
                new File("src/main/resources/schema/unilingual/" + language.key(), language.getSchemaDefinitionName() + ".gql"))
        println "Completed in: " + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s"
    }
}
