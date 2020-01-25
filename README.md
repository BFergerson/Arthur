# Arthur

[![Build Status](https://travis-ci.com/CodeBrig/Arthur.svg?branch=master)](https://travis-ci.com/CodeBrig/Arthur)

Arthur is a semantic language-agnostic UAST (universal abstract syntax tree) schema generator which uses source code as input and outputs unilingual and omnilingual ontologies derived from those language(s).
Arthur parses source code using [Babelfish](https://github.com/bblfsh/bblfshd) and constructs the observed schema for use in a [Grakn](https://github.com/graknlabs/grakn) knowledge graph. 

# Schemas

## Omnilingual Schema

| Languages                               | Segments                                                     |
| --------------------------------------- | ------------------------------------------------------------ |
| Bash, C++, C#, Go, Java, JavaScript, PHP, Python, Ruby | [Arthur_Omnilingual_Base_Structure.gql](https://github.com/CodeBrig/Arthur/blob/master/src/main/resources/schema/omnilingual/Arthur_Omnilingual_Base_Structure.gql) <br> [Arthur_Omnilingual_Semantic_Roles.gql](https://github.com/CodeBrig/Arthur/blob/master/src/main/resources/schema/omnilingual/Arthur_Omnilingual_Semantic_Roles.gql) |

## Unilingual Schemas

| Language   | Segments                                                     |
| ---------- | ------------------------------------------------------------ |
| Bash       | [Arthur_Bash_Base_Structure.gql](https://github.com/CodeBrig/Arthur/blob/master/src/main/resources/schema/unilingual/bash/Arthur_Bash_Base_Structure.gql) <br> [Arthur_Bash_Semantic_Roles.gql](https://github.com/CodeBrig/Arthur/blob/master/src/main/resources/schema/unilingual/bash/Arthur_Bash_Semantic_Roles.gql) |
| C++        | [Arthur_Cplusplus_Base_Structure.gql](https://github.com/CodeBrig/Arthur/blob/master/src/main/resources/schema/unilingual/cplusplus/Arthur_Cplusplus_Base_Structure.gql) <br> [Arthur_Cplusplus_Semantic_Roles.gql](https://github.com/CodeBrig/Arthur/blob/master/src/main/resources/schema/unilingual/cplusplus/Arthur_Cplusplus_Semantic_Roles.gql) |
| C#         | [Arthur_Csharp_Base_Structure.gql](https://github.com/CodeBrig/Arthur/blob/master/src/main/resources/schema/unilingual/csharp/Arthur_Csharp_Base_Structure.gql) <br> [Arthur_Csharp_Semantic_Roles.gql](https://github.com/CodeBrig/Arthur/blob/master/src/main/resources/schema/unilingual/csharp/Arthur_Csharp_Semantic_Roles.gql) |
| Go         | [Arthur_Go_Base_Structure.gql](https://github.com/CodeBrig/Arthur/blob/master/src/main/resources/schema/unilingual/go/Arthur_Go_Base_Structure.gql) <br> [Arthur_Go_Semantic_Roles.gql](https://github.com/CodeBrig/Arthur/blob/master/src/main/resources/schema/unilingual/go/Arthur_Go_Semantic_Roles.gql) |
| Java       | [Arthur_Java_Base_Structure.gql](https://github.com/CodeBrig/Arthur/blob/master/src/main/resources/schema/unilingual/java/Arthur_Java_Base_Structure.gql) <br> [Arthur_Java_Semantic_Roles.gql](https://github.com/CodeBrig/Arthur/blob/master/src/main/resources/schema/unilingual/java/Arthur_Java_Semantic_Roles.gql) |
| JavaScript | [Arthur_Javascript_Base_Structure.gql](https://github.com/CodeBrig/Arthur/blob/master/src/main/resources/schema/unilingual/javascript/Arthur_Javascript_Base_Structure.gql) <br> [Arthur_Javascript_Semantic_Roles.gql](https://github.com/CodeBrig/Arthur/blob/master/src/main/resources/schema/unilingual/javascript/Arthur_Javascript_Semantic_Roles.gql) |
| PHP        | [Arthur_Php_Base_Structure.gql](https://github.com/CodeBrig/Arthur/blob/master/src/main/resources/schema/unilingual/php/Arthur_Php_Base_Structure.gql) <br> [Arthur_Php_Semantic_Roles.gql](https://github.com/CodeBrig/Arthur/blob/master/src/main/resources/schema/unilingual/php/Arthur_Php_Semantic_Roles.gql) |
| Python     | [Arthur_Python_Base_Structure.gql](https://github.com/CodeBrig/Arthur/blob/master/src/main/resources/schema/unilingual/python/Arthur_Python_Base_Structure.gql) <br> [Arthur_Python_Semantic_Roles.gql](https://github.com/CodeBrig/Arthur/blob/master/src/main/resources/schema/unilingual/python/Arthur_Python_Semantic_Roles.gql) |
| Ruby       | [Arthur_Ruby_Base_Structure.gql](https://github.com/CodeBrig/Arthur/blob/master/src/main/resources/schema/unilingual/ruby/Arthur_Ruby_Base_Structure.gql) <br> [Arthur_Ruby_Semantic_Roles.gql](https://github.com/CodeBrig/Arthur/blob/master/src/main/resources/schema/unilingual/ruby/Arthur_Ruby_Semantic_Roles.gql) |

# Supported Concepts

## Structural

### Conditional

- If/ElseIf/Else
- Switch/SwitchCase

### Exception

- Try/Catch/Finally

### Loop

- ForLoop
- ForEachLoop
- WhileLoop
- DoWhileLoop

### Operator

#### Logical

- AndOperator
- OrOperator

#### Misc

- TernaryOperator

#### Relational

- RelationalOperator

##### Compare

- IsEqualOperator/IsNotEqualOperator
- IsEqualTypeOperator/IsNotEqualTypeOperator

##### Define

- DeclareVariableOperator
- InitializeVariableOperator

## Misc

- Child
- Function
- InternalRole
- Language
- Literal
- Multi
- Name
- Role
- Token
- Type
- Wildcard