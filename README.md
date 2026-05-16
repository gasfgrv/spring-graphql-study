# spring-graphql-study (Levantamento)

Ajustes necessários:

- testes

---

✦ Para garantir a qualidade e a robustez da sua aplicação Spring GraphQL seguindo a arquitetura hexagonal que você implementou, aqui estão as
  classes que você deve testar e os tipos de testes recomendados para cada uma:

  1. Camada de Aplicação (aplication.usecase)
  Tipo de Teste: Unitário (utilizando JUnit 5 e Mockito).

   * Classes: CourseUsecase, StudentUsecase, EnrollmentUsecase.
   * O que testar:
       * Regras de negócio e lógica de decisão.
       * Se as chamadas aos repositórios (Portas de Saída) estão ocorrendo com os parâmetros corretos.
       * Se as exceções customizadas (ex: CourseNotFoundException) são lançadas em cenários de erro.
       * Caminhos felizes e fluxos de erro.

  2. Camada de Infraestrutura - Resolvers (infrastructure.resolver)
  Tipo de Teste: Integração / Slice Test (utilizando @GraphQlTest).
  
   * Classes: CourseResolver, StudentResolver, EnrollmentResolver.
   * O que testar:
       * Mapeamento das @QueryMapping, @MutationMapping e @BatchMapping.
       * Validação dos argumentos recebidos via @Argument.
       * Garantir que o retorno do Resolver corresponde ao esperado pelo schema.graphqls.
       * Testar o GlobalExceptionHandler enviando requisições que forcem erros e verificando se o formato do erro GraphQL está correto.

  3. Camada de Infraestrutura - Adaptadores (infrastructure.adapter)
  Tipo de Teste: Integração (utilizando @DataJpaTest e, idealmente, Testcontainers).
  
   * Classes: CourseRepository, StudentRepository, EnrollmentRepository.
   * O que testar:
       * A integração real com o banco de dados.
       * Persistência de entidades e recuperação correta.
       * Se o Mapper está convertendo corretamente de Entidade para Objeto de Domínio e vice-versa durante as operações de salvamento e busca.

  4. Camada de Infraestrutura - Mappers (infrastructure.adapter.*.Mapper)
  Tipo de Teste: Unitário.
  
   * Classes: CourseMapper, StudentMapper, EnrollmentMapper.
   * O que testar:
       * Garantir que todos os campos estão sendo copiados corretamente entre as camadas (Domain ↔ Entity).
       * Casos de campos nulos ou conversões de tipos específicos.

  5. Domínio - Modelos (domain.model)
  Tipo de Teste: Unitário.
  
   * Classes: Course, Student, Enrollment.
   * O que testar:
       * Como vi no seu CourseUsecase que você usa métodos como .validateTitle(), esses comportamentos devem ser testados aqui.
       * Validações internas de estado do objeto e lógica rica de domínio.

  6. Componentes de Suporte (infrastructure.graphql)
  Tipo de Teste: Unitário.
  
   * Classe: LocalDateTimeCoercing.
   * O que testar:
       * A lógica de serialização (Java -> String/ISO) e desserialização (String -> Java) de datas
