# spring-graphql-study (Levantamento)

Uma boa ideia para praticar **Spring Boot + GraphQL** com até 3 entidades é fazer um sistema que tenha:

* relações entre entidades
* queries e mutations interessantes
* paginação/filtros
* possibilidade de evoluir depois

## Sugestão: Sistema de Cursos Online

### Entidades

#### 1. `Student`

```java
id
name
email
createdAt
```

#### 2. `Course`

```java
id
title
description
level
```

#### 3. `Enrollment`

(relaciona aluno e curso)

```java
id
progress
enrolledAt
student
course
```

---

## Por que esse projeto é bom para GraphQL?

Você consegue praticar:

* relacionamentos (`Student -> Enrollment -> Course`)
* nested queries
* mutations com input types
* filtros
* paginação
* DataLoader/N+1
* DTOs
* validação
* segurança depois (JWT)

---

## Exemplos de Queries

### Buscar aluno com cursos

```graphql
query {
  student(id: 1) {
    name
    enrollments {
      progress
      course {
        title
        level
      }
    }
  }
}
```

---

### Buscar cursos por nível

```graphql
query {
  courses(level: "BEGINNER") {
    title
    description
  }
}
```

---

## Exemplos de Mutations

### Criar aluno

```graphql
mutation {
  createStudent(input: {
    name: "Lucas"
    email: "lucas@email.com"
  }) {
    id
    name
  }
}
```

---

### Matricular aluno em curso

```graphql
mutation {
  enrollStudent(input: {
    studentId: 1
    courseId: 2
  }) {
    id
    enrolledAt
  }
}
```

---

## Stack sugerida

* Java 21
* Spring Boot 3
* Spring for GraphQL
* Spring Data JPA
* PostgreSQL
* Flyway
* Lombok
* MapStruct (opcional)

---

## Estrutura interessante

```text
graphql/
 ├── schema.graphqls

student/
 ├── StudentEntity
 ├── StudentRepository
 ├── StudentService
 ├── StudentController

course/
enrollment/
```

---

## O que você vai aprender na prática

### GraphQL

* `Query`
* `Mutation`
* `Input`
* relacionamentos
* resolvers
* schema-first

### Spring

* JPA relations
* services
* transactions
* validation
* exception handling

### Banco

* `@ManyToOne`
* `@OneToMany`
* chaves compostas (se quiser)

---

## Evoluções futuras

Depois você pode adicionar:

* autenticação JWT
* roles (`ADMIN`, `STUDENT`)
* upload de certificado
* subscriptions GraphQL
* cache
* Docker
* testes integrados

---

## Outra ideia (mais “enterprise”)

### Sistema de Biblioteca

Entidades:

* `Book`
* `Author`
* `Loan`

Também fica excelente para GraphQL.

---

## Outra ideia (mais moderna)

### Task Management estilo Trello

Entidades:

* `User`
* `Board`
* `Task`

Muito bom para praticar filtros e mutations.

---
