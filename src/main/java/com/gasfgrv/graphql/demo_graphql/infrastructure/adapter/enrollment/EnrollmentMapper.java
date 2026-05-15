package com.gasfgrv.graphql.demo_graphql.infrastructure.adapter.enrollment;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import org.mapstruct.Mapper;

import com.gasfgrv.graphql.demo_graphql.domain.model.Enrollment;
import org.mapstruct.Mapping;

@Mapper(componentModel = SPRING)
public interface EnrollmentMapper {

    EnrollmentEntity toEntity(Enrollment enrollment);

    @Mapping(target = "student", expression = "java(entity.getStudent() != null ? com.gasfgrv.graphql.demo_graphql.domain.model.Student.builder().id(entity.getStudent().getId()).build() : null)")
    @Mapping(target = "course", expression = "java(entity.getCourse() != null ? com.gasfgrv.graphql.demo_graphql.domain.model.Course.builder().id(entity.getCourse().getId()).build() : null)")
    Enrollment toDomain(EnrollmentEntity entity);

}
