package com.gasfgrv.graphql.demo_graphql.infrastructure.adapter.course;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.gasfgrv.graphql.demo_graphql.domain.model.Course;
import com.gasfgrv.graphql.demo_graphql.infrastructure.adapter.enrollment.EnrollmentMapper;

@Mapper(componentModel = SPRING, uses = EnrollmentMapper.class)
public interface CourseMapper {

    CourseEntity toEntity(Course course);

    @Mapping(target = "validateTitle", ignore = true)
    @Mapping(target = "validateLevel", ignore = true)
    @Mapping(target = "validateDescription", ignore = true)
    Course toDomain(CourseEntity entity);

}
