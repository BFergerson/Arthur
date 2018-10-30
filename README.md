# OmniSRC
OmniSRC is a semantic omnilingual UAST (universal abstract syntax tree) schema generator which uses source code as input and outputs unilingual and omnilingual ontologies derived from those language(s).
OmniSRC parses source code using [Babelfish](https://github.com/bblfsh/bblfshd) and stores the resulting information in a [Grakn](https://github.com/graknlabs/grakn) knowledge graph. 

## Omnilingual Schemas

| Languages   | Version     | Schema      |
| ----------- | ----------- | ----------- |
| Go, Java, Javascript, Php, Python, Ruby | v1 | [OmniSRC_Omnilingual_Schema-v1.gql](https://github.com/CodeBrig/OmniSRC/blob/master/src/main/resources/schema/omnilingual/OmniSRC_Omnilingual_Schema-v1.gql) |

## Unilingual Schemas

| Language    | Version     | Schema      |
| ----------- | ----------- | ----------- |
| Go          | v1          | [OmniSRC_Go_Schema-v1.gql](https://github.com/CodeBrig/OmniSRC/blob/master/src/main/resources/schema/unilingual/go/OmniSRC_Go_Schema-v1.gql) |
| Java        | v1          | [OmniSRC_Java_Schema-v1.gql](https://github.com/CodeBrig/OmniSRC/blob/master/src/main/resources/schema/unilingual/java/OmniSRC_Java_Schema-v1.gql) |
| Javascript  | v1          | [OmniSRC_Javascript_Schema-v1.gql](https://github.com/CodeBrig/OmniSRC/blob/master/src/main/resources/schema/unilingual/javascript/OmniSRC_Javascript_Schema-v1.gql) |
| Php         | v1          | [OmniSRC_Php_Schema-v1.gql](https://github.com/CodeBrig/OmniSRC/blob/master/src/main/resources/schema/unilingual/php/OmniSRC_Php_Schema-v1.gql) |
| Python      | v1          | [OmniSRC_Python_Schema-v1.gql](https://github.com/CodeBrig/OmniSRC/blob/master/src/main/resources/schema/unilingual/python/OmniSRC_Python_Schema-v1.gql) |
| Ruby        | v1          | [OmniSRC_Ruby_Schema-v1.gql](https://github.com/CodeBrig/OmniSRC/blob/master/src/main/resources/schema/unilingual/ruby/OmniSRC_Ruby_Schema-v1.gql) |
