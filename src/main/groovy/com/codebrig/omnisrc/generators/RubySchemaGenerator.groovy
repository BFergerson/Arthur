package com.codebrig.omnisrc.generators

import com.codebrig.omnisrc.SourceLanguage

import java.util.concurrent.TimeUnit

/**
 * todo: description
 *
 * @version 0.1
 * @since 0.1
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 */
class RubySchemaGenerator extends OmniSchemaGenerator {

    public static final SourceLanguage language = SourceLanguage.Ruby
    public static final int PARSE_PROJECTS = 30

    static void main(String[] args) {
        long startTime = System.currentTimeMillis()
        generateUnilingualSchema(language, PARSE_PROJECTS,
                new File("src/main/resources/schema/unilingual/" + language.key(), language.getSchemaDefinitionName() + ".gql"))
        println "Completed in: " + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) + "s"
    }
}
