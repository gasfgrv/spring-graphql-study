package com.gasfgrv.graphql.demo_graphql.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import com.gasfgrv.graphql.demo_graphql.infrastructure.graphql.LocalDateTimeCoercing;

import graphql.schema.GraphQLScalarType;

@Configuration
public class GraphQLConfig {

    @Bean
    public GraphQLScalarType localDateTimeScalar() {
        return GraphQLScalarType.newScalar()
                .name("DateTime")
                .description("Scalar for timestamps")
                .coercing(new LocalDateTimeCoercing())
                .build();
    }

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer(GraphQLScalarType graphQLScalarType) {
        return builder -> builder.scalar(graphQLScalarType);
    }

}
