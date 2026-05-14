package com.gasfgrv.graphql.demo_graphql.infrastructure.adapter.enrollment;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import org.mapstruct.Mapper;

import com.gasfgrv.graphql.demo_graphql.domain.model.Enrollment;
import org.mapstruct.Mapping;

@Mapper(componentModel = SPRING)
public interface EnrollmentMapper {

    EnrollmentEntity toEntity(Enrollment enrollment);

    @Mapping(target = "student.enrollments", ignore = true)
    @Mapping(target = "course.enrollments", ignore = true)
    Enrollment toDomain(EnrollmentEntity entity);

}
