package com.gasfgrv.graphql.demo_graphql.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gasfgrv.graphql.demo_graphql.aplication.usecase.CourseUsecase;
import com.gasfgrv.graphql.demo_graphql.aplication.usecase.EnrollmentUsecase;
import com.gasfgrv.graphql.demo_graphql.aplication.usecase.StudentUsecase;
import com.gasfgrv.graphql.demo_graphql.domain.port.in.CourseUsecasePort;
import com.gasfgrv.graphql.demo_graphql.domain.port.in.EnrollmentUsecasePort;
import com.gasfgrv.graphql.demo_graphql.domain.port.in.StudentUsecasePort;
import com.gasfgrv.graphql.demo_graphql.domain.port.out.CourseRepositoryPort;
import com.gasfgrv.graphql.demo_graphql.domain.port.out.EnrollmentRepositoryPort;
import com.gasfgrv.graphql.demo_graphql.domain.port.out.StudentRepositoryPort;

@Configuration
public class ApplicationConfig {

    @Bean
    public CourseUsecasePort courseUsecase(CourseRepositoryPort repository) {
        return new CourseUsecase(repository);
    }

    @Bean
    public StudentUsecasePort studentUsecase(StudentRepositoryPort repository) {
        return new StudentUsecase(repository);
    }

    @Bean
    public EnrollmentUsecasePort enrollmentUsecase(EnrollmentRepositoryPort enrollmentRepository,
            StudentRepositoryPort studentRepository, CourseRepositoryPort courseRepository) {
        return new EnrollmentUsecase(enrollmentRepository, studentRepository, courseRepository);
    }

}
