# API Restful em Kotlin

## Breve resumo da existencia deste repositório

Objetivo do projeto foi aprender sobre as funcionalidades que o Springboot em conjunto com o Kotlin oferecem para a criação de uma API Restful utilizando um curso como referência.

No curso, ([Udemy](https://www.udemy.com/course/kotlin-spring/)) foi abordado diversos tópicos e pacotes, entre eles, os principais destacados são:
- `Annotations` para criação de recursos `(@Controller, @Service, @Repository, @Component, @Bean, @Configuration, etc)`
- `Spring Events` para criação de eventos na aplicação como forma de segregar melhor as responsabilidades de cada recurso.
- Utilização do pacote `Spring Security` para implementação de uma autentição JWT
- Utilização do `JUnit` com `MockK` para criação de testes unitários
- Utilização do `Swagger OpenAPI` para criação de documentação automatizada.

## Sobre o projeto

Trata-se de uma api simples de compra de livros nomeado como `Mercado Livro` onde temos os seguintes recursos:

1. Customer
2. Books
3. Purchase
4. Admin (Apenas para validar o conceito de Roles do Spring Security)

O banco de dados utilizado neste projeto foi o MySQL de forma dockerizada simples, motivo do qual não possui `Dockerfile` ou `docker-compose.yaml` neste projeto.
