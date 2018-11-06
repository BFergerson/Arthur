package com.codebrig.omnisrc.generators.github

import com.codebrig.omnisrc.SourceLanguage

import java.util.concurrent.TimeUnit

/**
 * todo: description
 *
 * @version 0.2
 * @since 0.1
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class PhpSchemaGenerator extends OmniSchemaGenerator {

    public static final SourceLanguage language = SourceLanguage.Php
    public static final int PARSE_PROJECTS = 3

    static void main(String[] args) {
        long startTime = System.currentTimeMillis()
        generateUnilingualSchema(language, PARSE_PROJECTS,
                new File("src/main/resources/schema/unilingual/" + language.key(), language.getSchemaDefinitionName() + ".gql"))
        println "Completed in: " + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s"
    }
}
