package com.gasfgrv.graphql.demo_graphql.infrastructure.adapter.student;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.gasfgrv.graphql.demo_graphql.domain.model.Student;
import com.gasfgrv.graphql.demo_graphql.infrastructure.adapter.enrollment.EnrollmentMapper;

@Mapper(componentModel = SPRING, uses = EnrollmentMapper.class)
public interface StudentMapper {

    @Mapping(target = "enrollments", source = "enrollment")
    StudentEntity toEntity(Student student);

    @Mapping(target = "validateName", ignore = true)
    @Mapping(target = "validateEmail", ignore = true)
    @Mapping(target = "enrollment", source = "enrollments")
    Student toDomain(StudentEntity entity);

}
