# OmniSRC
OmniSRC is a semantic omnilingual UAST (universal abstract syntax tree) schema generator which uses source code as input and outputs unilingual and omnilingual ontologies derived from those language(s).
OmniSRC parses source code using [Babelfish](https://github.com/bblfsh/bblfshd) and stores the resulting ontology as a [Grakn](https://github.com/graknlabs/grakn) schema definition. 

## Omnilingual Schemas

| Languages   | Schema      |
| ----------- | ----------- |
| Go, Java, Javascript, Php, Python, Ruby | [OmniSRC_Omnilingual_Base_Structure.gql](https://github.com/CodeBrig/OmniSRC/blob/master/src/main/resources/schema/omnilingual/OmniSRC_Omnilingual_Base_Structure.gql) <br> [OmniSRC_Omnilingual_Individual_Semantic_Roles.gql](https://github.com/CodeBrig/OmniSRC/blob/master/src/main/resources/schema/omnilingual/OmniSRC_Omnilingual_Individual_Semantic_Roles.gql) <br> [OmniSRC_Omnilingual_Actual_Semantic_Roles.gql](https://github.com/CodeBrig/OmniSRC/blob/master/src/main/resources/schema/omnilingual/OmniSRC_Omnilingual_Actual_Semantic_Roles.gql) <br> [OmniSRC_Omnilingual_Possible_Semantic_Roles.gql](https://github.com/CodeBrig/OmniSRC/blob/master/src/main/resources/schema/omnilingual/OmniSRC_Omnilingual_Possible_Semantic_Roles.gql)

## Unilingual Schemas

| Language    | Schema      |
| ----------- | ----------- |
| Go          | [OmniSRC_Go_Schema-1.0.gql](https://github.com/CodeBrig/OmniSRC/blob/master/src/main/resources/schema/unilingual/go/OmniSRC_Go_Schema-1.0.gql) |
| Java        | [OmniSRC_Java_Schema-1.0.gql](https://github.com/CodeBrig/OmniSRC/blob/master/src/main/resources/schema/unilingual/java/OmniSRC_Java_Schema-1.0.gql) |
| Javascript  | [OmniSRC_Javascript_Schema-1.0.gql](https://github.com/CodeBrig/OmniSRC/blob/master/src/main/resources/schema/unilingual/javascript/OmniSRC_Javascript_Schema-1.0.gql) |
| Php         | [OmniSRC_Php_Schema-1.0.gql](https://github.com/CodeBrig/OmniSRC/blob/master/src/main/resources/schema/unilingual/php/OmniSRC_Php_Schema-1.0.gql) |
| Python      | [OmniSRC_Python_Schema-1.0.gql](https://github.com/CodeBrig/OmniSRC/blob/master/src/main/resources/schema/unilingual/python/OmniSRC_Python_Schema-1.0.gql) |
| Ruby        | [OmniSRC_Ruby_Schema-1.0.gql](https://github.com/CodeBrig/OmniSRC/blob/master/src/main/resources/schema/unilingual/ruby/OmniSRC_Ruby_Schema-1.0.gql) |
