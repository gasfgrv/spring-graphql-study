package com.gasfgrv.graphql.demo_graphql.infrastructure.adapter.student;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import org.mapstruct.Mapper;

import com.gasfgrv.graphql.demo_graphql.domain.model.Student;
import com.gasfgrv.graphql.demo_graphql.infrastructure.adapter.enrollment.EnrollmentMapper;

@Mapper(componentModel = SPRING, uses = EnrollmentMapper.class)
public interface StudentMapper {

    StudentEntity toEntity(Student student);

    Student toDomain(StudentEntity entity);

}
