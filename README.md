# OmniSRC
OmniSRC is a semantic omnilingual UAST (universal abstract syntax tree) schema generator which uses source code as input and outputs unilingual and omnilingual ontologies derived from those language(s).
OmniSRC parses source code using [Babelfish](https://github.com/bblfsh/bblfshd) and stores the resulting ontology as a [Grakn](https://github.com/graknlabs/grakn) schema definition. 

## Omnilingual Schemas

| Languages   | Version     | Schema      |
| ----------- | ----------- | ----------- |
| Go, Java, Javascript, Php, Python, Ruby | 1.0 | [OmniSRC_Omnilingual_Schema-1.0.gql](https://github.com/CodeBrig/OmniSRC/blob/master/src/main/resources/schema/omnilingual/OmniSRC_Omnilingual_Schema-1.0.gql) |

## Unilingual Schemas

| Language    | Version     | Schema      |
| ----------- | ----------- | ----------- |
| Go          | 1.0         | [OmniSRC_Go_Schema-1.0.gql](https://github.com/CodeBrig/OmniSRC/blob/master/src/main/resources/schema/unilingual/go/OmniSRC_Go_Schema-1.0.gql) |
| Java        | 1.0         | [OmniSRC_Java_Schema-1.0.gql](https://github.com/CodeBrig/OmniSRC/blob/master/src/main/resources/schema/unilingual/java/OmniSRC_Java_Schema-1.0.gql) |
| Javascript  | 1.0         | [OmniSRC_Javascript_Schema-1.0.gql](https://github.com/CodeBrig/OmniSRC/blob/master/src/main/resources/schema/unilingual/javascript/OmniSRC_Javascript_Schema-1.0.gql) |
| Php         | 1.0         | [OmniSRC_Php_Schema-1.0.gql](https://github.com/CodeBrig/OmniSRC/blob/master/src/main/resources/schema/unilingual/php/OmniSRC_Php_Schema-1.0.gql) |
| Python      | 1.0         | [OmniSRC_Python_Schema-1.0.gql](https://github.com/CodeBrig/OmniSRC/blob/master/src/main/resources/schema/unilingual/python/OmniSRC_Python_Schema-1.0.gql) |
| Ruby        | 1.0         | [OmniSRC_Ruby_Schema-1.0.gql](https://github.com/CodeBrig/OmniSRC/blob/master/src/main/resources/schema/unilingual/ruby/OmniSRC_Ruby_Schema-1.0.gql) |
