package com.gasfgrv.graphql.demo_graphql.domain.port.in;

import java.util.List;
import java.util.Optional;

import com.gasfgrv.graphql.demo_graphql.domain.model.Course;

public interface CourseUsecasePort {

    List<Course> findAllCourses();

    Optional<Course> findCourseById(long id);

    Course createCourse(String title, String description, String level);

    Course updateCourse(long id, String title, String description, String level);

    boolean deleteCourse(long id);

}
