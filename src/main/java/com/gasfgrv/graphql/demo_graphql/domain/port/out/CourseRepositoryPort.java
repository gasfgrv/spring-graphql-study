package com.gasfgrv.graphql.demo_graphql.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.gasfgrv.graphql.demo_graphql.domain.model.Course;

public interface CourseRepositoryPort {

    List<Course> findAll();

    Optional<Course> findById(long id);

    Course save(Course course);

    boolean deleteById(long id);

}
