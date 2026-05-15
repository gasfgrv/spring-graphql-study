package com.gasfgrv.graphql.demo_graphql.infrastructure.resolver;

import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.web.bind.annotation.ControllerAdvice;

import com.gasfgrv.graphql.demo_graphql.aplication.exceptions.CourseNotFoundException;
import com.gasfgrv.graphql.demo_graphql.aplication.exceptions.EnrollmentNotFoundException;
import com.gasfgrv.graphql.demo_graphql.aplication.exceptions.StudentNotFoundException;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @GraphQlExceptionHandler({
            CourseNotFoundException.class,
            EnrollmentNotFoundException.class,
            StudentNotFoundException.class
    })
    public GraphQLError handleNotFound(RuntimeException ex, DataFetchingEnvironment env) {
        log.error("Resource not found: {}", ex.getMessage());
        return GraphqlErrorBuilder.newError(env)
                .message(ex.getMessage())
                .errorType(ErrorType.NOT_FOUND)
                .build();
    }

    @GraphQlExceptionHandler(IllegalArgumentException.class)
    public GraphQLError handleValidation(IllegalArgumentException ex, DataFetchingEnvironment env) {
        log.error("Validation error: {}", ex.getMessage());
        return GraphqlErrorBuilder.newError(env)
                .message(ex.getMessage())
                .errorType(ErrorType.BAD_REQUEST)
                .build();
    }

    @GraphQlExceptionHandler
    public GraphQLError handleGeneral(Exception ex, DataFetchingEnvironment env) {
        log.error("Internal server error: {}", ex.getMessage(), ex);
        return GraphqlErrorBuilder.newError(env)
                .message("Internal server error")
                .errorType(ErrorType.INTERNAL_ERROR)
                .build();
    }

}
