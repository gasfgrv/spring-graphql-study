package com.gasfgrv.graphql.demo_graphql.infrastructure.adapter.student;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.gasfgrv.graphql.demo_graphql.domain.model.Student;
import com.gasfgrv.graphql.demo_graphql.infrastructure.adapter.enrollment.EnrollmentMapper;

@Mapper(componentModel = SPRING, uses = EnrollmentMapper.class)
public interface StudentMapper {

    StudentEntity toEntity(Student student);

    @Mapping(target = "enrollments", ignore = true)
    Student toDomain(StudentEntity entity);

}
