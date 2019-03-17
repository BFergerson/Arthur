# OmniSRC

[![Build Status](https://travis-ci.com/CodeBrig/OmniSRC.svg?branch=master)](https://travis-ci.com/CodeBrig/OmniSRC)

OmniSRC is a semantic omnilingual UAST (universal abstract syntax tree) schema generator which uses source code as input and outputs unilingual and omnilingual ontologies derived from those language(s).
OmniSRC parses source code using [Babelfish](https://github.com/bblfsh/bblfshd) and constructs the observed schema for use in a [Grakn](https://github.com/graknlabs/grakn) knowledge graph. 

## Omnilingual Schema

| Languages   | Segments    |
| ----------- | ----------- |
| Go, Java, JavaScript, PHP, Python, Ruby | [OmniSRC_Omnilingual_Base_Structure.gql](https://github.com/CodeBrig/OmniSRC/blob/master/src/main/resources/schema/omnilingual/OmniSRC_Omnilingual_Base_Structure.gql) <br> [OmniSRC_Omnilingual_Individual_Semantic_Roles.gql](https://github.com/CodeBrig/OmniSRC/blob/master/src/main/resources/schema/omnilingual/OmniSRC_Omnilingual_Individual_Semantic_Roles.gql) <br> [OmniSRC_Omnilingual_Actual_Semantic_Roles.gql](https://github.com/CodeBrig/OmniSRC/blob/master/src/main/resources/schema/omnilingual/OmniSRC_Omnilingual_Actual_Semantic_Roles.gql) |

## Unilingual Schemas

| Language    | Segments    |
| ----------- | ----------- |
| Go          | [OmniSRC_Go_Base_Structure.gql](https://github.com/CodeBrig/OmniSRC/blob/master/src/main/resources/schema/unilingual/go/OmniSRC_Go_Base_Structure.gql) <br> [OmniSRC_Go_Individual_Semantic_Roles.gql](https://github.com/CodeBrig/OmniSRC/blob/master/src/main/resources/schema/unilingual/go/OmniSRC_Go_Individual_Semantic_Roles.gql) <br> [OmniSRC_Go_Actual_Semantic_Roles.gql](https://github.com/CodeBrig/OmniSRC/blob/master/src/main/resources/schema/unilingual/go/OmniSRC_Go_Actual_Semantic_Roles.gql) |
| Java        | [OmniSRC_Java_Base_Structure.gql](https://github.com/CodeBrig/OmniSRC/blob/master/src/main/resources/schema/unilingual/java/OmniSRC_Java_Base_Structure.gql) <br> [OmniSRC_Java_Individual_Semantic_Roles.gql](https://github.com/CodeBrig/OmniSRC/blob/master/src/main/resources/schema/unilingual/java/OmniSRC_Java_Individual_Semantic_Roles.gql) <br> [OmniSRC_Java_Actual_Semantic_Roles.gql](https://github.com/CodeBrig/OmniSRC/blob/master/src/main/resources/schema/unilingual/java/OmniSRC_Java_Actual_Semantic_Roles.gql) |
| JavaScript  | [OmniSRC_Javascript_Base_Structure.gql](https://github.com/CodeBrig/OmniSRC/blob/master/src/main/resources/schema/unilingual/javascript/OmniSRC_Javascript_Base_Structure.gql) <br> [OmniSRC_Javascript_Individual_Semantic_Roles.gql](https://github.com/CodeBrig/OmniSRC/blob/master/src/main/resources/schema/unilingual/javascript/OmniSRC_Javascript_Individual_Semantic_Roles.gql) <br> [OmniSRC_Javascript_Actual_Semantic_Roles.gql](https://github.com/CodeBrig/OmniSRC/blob/master/src/main/resources/schema/unilingual/javascript/OmniSRC_Javascript_Actual_Semantic_Roles.gql) |
| PHP         | [OmniSRC_Php_Base_Structure.gql](https://github.com/CodeBrig/OmniSRC/blob/master/src/main/resources/schema/unilingual/php/OmniSRC_Php_Base_Structure.gql) <br> [OmniSRC_Php_Individual_Semantic_Roles.gql](https://github.com/CodeBrig/OmniSRC/blob/master/src/main/resources/schema/unilingual/php/OmniSRC_Php_Individual_Semantic_Roles.gql) <br> [OmniSRC_Php_Actual_Semantic_Roles.gql](https://github.com/CodeBrig/OmniSRC/blob/master/src/main/resources/schema/unilingual/php/OmniSRC_Php_Actual_Semantic_Roles.gql) |
| Python      | [OmniSRC_Python_Base_Structure.gql](https://github.com/CodeBrig/OmniSRC/blob/master/src/main/resources/schema/unilingual/python/OmniSRC_Python_Base_Structure.gql) <br> [OmniSRC_Python_Individual_Semantic_Roles.gql](https://github.com/CodeBrig/OmniSRC/blob/master/src/main/resources/schema/unilingual/python/OmniSRC_Python_Individual_Semantic_Roles.gql) <br> [OmniSRC_Python_Actual_Semantic_Roles.gql](https://github.com/CodeBrig/OmniSRC/blob/master/src/main/resources/schema/unilingual/python/OmniSRC_Python_Actual_Semantic_Roles.gql) |
| Ruby        | [OmniSRC_Ruby_Base_Structure.gql](https://github.com/CodeBrig/OmniSRC/blob/master/src/main/resources/schema/unilingual/ruby/OmniSRC_Ruby_Base_Structure.gql) <br> [OmniSRC_Ruby_Individual_Semantic_Roles.gql](https://github.com/CodeBrig/OmniSRC/blob/master/src/main/resources/schema/unilingual/ruby/OmniSRC_Ruby_Individual_Semantic_Roles.gql) <br> [OmniSRC_Ruby_Actual_Semantic_Roles.gql](https://github.com/CodeBrig/OmniSRC/blob/master/src/main/resources/schema/unilingual/ruby/OmniSRC_Ruby_Actual_Semantic_Roles.gql) |
