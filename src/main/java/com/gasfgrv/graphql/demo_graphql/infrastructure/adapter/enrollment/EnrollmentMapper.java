package com.gasfgrv.graphql.demo_graphql.infrastructure.adapter.enrollment;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import org.mapstruct.Mapper;

import com.gasfgrv.graphql.demo_graphql.domain.model.Enrollment;

@Mapper(componentModel = SPRING)
public interface EnrollmentMapper {

    EnrollmentEntity toEntity(Enrollment enrollment);

    Enrollment toDomain(EnrollmentEntity entity);

}
