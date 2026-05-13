package com.gasfgrv.graphql.demo_graphql.infrastructure.adapter.enrollment;

import com.gasfgrv.graphql.demo_graphql.domain.model.Enrollment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface EnrollmentMapper {

    EnrollmentEntity toEntity(Enrollment enrollment);

    @Mapping(target = "updateProgress", ignore = true)
    Enrollment toDomain(EnrollmentEntity entity);

}
