# Estudo de Spring Boot com GraphQL

Este projeto é um estudo prático de implementação de uma API GraphQL utilizando Spring Boot, seguindo os princípios da **Arquitetura Hexagonal** (Ports and Adapters) e **Domain-Driven Design (DDD)**.

## Arquitetura do Projeto

O projeto utiliza a arquitetura hexagonal para garantir o desacoplamento entre a lógica de negócio e as tecnologias externas (Banco de Dados, Interface GraphQL).

```mermaid
graph TD
    subgraph "Infrastructure Layer (Adapters)"
        GraphQL[GraphQL Resolvers]
        JPA[JPA Repositories]
        Mappers[MapStruct Mappers]
    end

    subgraph "Application Layer"
        UC[Use Cases]
    end

    subgraph "Domain Layer"
        P_IN[Input Ports - Interfaces]
        P_OUT[Output Ports - Interfaces]
        Models[Domain Models / Entities]
    end

    GraphQL --> P_IN
    P_IN --> UC
    UC --> Models
    UC --> P_OUT
    P_OUT --> JPA
    JPA -.-> Mappers
```

### Camadas

- **Domain:** Contém as entidades de negócio, regras de validação e as interfaces (Ports) que definem como o mundo externo interage com o core e vice-versa.
- **Application:** Implementa os casos de uso (Use Cases) que orquestram a lógica de negócio.
- **Infrastructure:** Implementa os adaptadores para tecnologias específicas, como o GraphQL para entrada de dados e JPA para persistência.

---

## Modelo de Dados (ER)

O domínio do sistema foca em Estudantes, Cursos e Matrículas (Enrollments).

```mermaid
erDiagram
    STUDENT ||--o{ ENROLLMENT : possui
    COURSE ||--o{ ENROLLMENT : possui
    
    STUDENT {
        long id PK
        string name
        string email
        datetime createdAt
    }
    
    COURSE {
        long id PK
        string title
        string description
        string level
    }
    
    ENROLLMENT {
        long id PK
        long studentId FK
        long courseId FK
        int progress
        datetime enrolledAt
    }
```

---

## Fluxo de Execução da API

Abaixo, um diagrama de sequência exemplificando o fluxo de criação de uma matrícula:

```mermaid
sequenceDiagram
    participant Client
    participant Resolver as EnrollmentResolver
    participant PortIn as EnrollmentUsecasePort
    participant UseCase as EnrollmentUsecase
    participant PortOut as EnrollmentRepositoryPort
    participant DB as Database

    Client->>Resolver: Mutation: createEnrollment(studentId, courseId)
    Resolver->>PortIn: execute(studentId, courseId)
    PortIn->>UseCase: createEnrollment(...)
    UseCase->>PortOut: findStudentById(studentId)
    PortOut-->>UseCase: Student Object
    UseCase->>PortOut: findCourseById(courseId)
    PortOut-->>UseCase: Course Object
    UseCase->>UseCase: Validate & Create Enrollment
    UseCase->>PortOut: save(Enrollment)
    PortOut->>DB: INSERT INTO enrollments
    DB-->>PortOut: Success
    PortOut-->>UseCase: Saved Enrollment
    UseCase-->>PortIn: Saved Enrollment
    PortIn-->>Resolver: Saved Enrollment
    Resolver-->>Client: GraphQL Response
```

---

## Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 4.x** (Spring Framework 6.x)
- **Spring for GraphQL**
- **Spring Data JPA**
- **PostgreSQL**
- **Flyway** (Migrações de banco de dados)
- **MapStruct** (Mapeamento de entidades)
- **Lombok**
- **Testcontainers** (Testes de integração com banco real)
- **Docker & Docker Compose**

---

## Como Executar

### Pré-requisitos

- Docker e Docker Compose instalados.
- JDK 21+.

### Passos

1. **Subir o Banco de Dados:**
   O projeto utiliza `spring-boot-docker-compose`, então ao iniciar a aplicação, o container do PostgreSQL subirá automaticamente se o Docker estiver rodando. Caso queira subir manualmente:

   ```bash
   docker-compose up -d
   ```

2. **Executar a Aplicação:**

   ```bash
   ./mvnw spring-boot:run
   ```

3. **Acessar o GraphiQL:**
   A interface para testes da API estará disponível em:
   `http://localhost:8080/graphiql`

---

## Exemplos de Uso (GraphQL)

### Criar um novo Estudante

```graphql
mutation {
  createStudent(name: "João Silva", email: "joao@email.com") {
    id
    name
    createdAt
  }
}
```

### Criar um Curso

```graphql
mutation {
  createCourse(
    title: "Spring Boot com GraphQL"
    description: "Aprenda a criar APIs modernas com Spring e GraphQL"
    level: "Intermediário"
  ) {
    id
    title
    level
  }
}
```

### Realizar uma Matrícula

```graphql
mutation {
  createEnrollment(studentId: 1, courseId: 1) {
    id
    progress
    enrolledAt
    student {
      name
    }
    course {
      title
    }
  }
}
```

### Consultar todos os Estudantes e suas Matrículas

```graphql
query {
  findAllStudents {
    name
    email
    enrollments {
      course {
        title
      }
      progress
    }
  }
}
```

---

## Detalhes Técnicos

### Custom Scalar: DateTime

O projeto implementa um scalar customizado `DateTime` para lidar com `LocalDateTime` do Java, garantindo a serialização correta no formato ISO-8601.

### Validações de Domínio

As validações de negócio estão localizadas diretamente nos modelos de domínio (`Student`, `Course`, `Enrollment`), garantindo que o estado das entidades seja sempre válido antes de serem persistidas.

### Tratamento de Exceções

Existe um `GlobalExceptionHandler` que captura exceções de domínio e as mapeia para erros amigáveis do GraphQL, utilizando a especificação de erros da linguagem.
