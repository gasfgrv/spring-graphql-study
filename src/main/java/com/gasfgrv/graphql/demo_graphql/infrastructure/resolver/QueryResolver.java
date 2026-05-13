package com.gasfgrv.graphql.demo_graphql.infrastructure.resolver;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.gasfgrv.graphql.demo_graphql.domain.model.Course;
import com.gasfgrv.graphql.demo_graphql.domain.model.Enrollment;
import com.gasfgrv.graphql.demo_graphql.domain.model.Student;
import com.gasfgrv.graphql.demo_graphql.domain.port.in.CourseUsecasePort;
import com.gasfgrv.graphql.demo_graphql.domain.port.in.EnrollmentUsecasePort;
import com.gasfgrv.graphql.demo_graphql.domain.port.in.StudentUsecasePort;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class QueryResolver {

    private final StudentUsecasePort studentUsecase;
    private final CourseUsecasePort courseUsecase;
    private final EnrollmentUsecasePort enrollmentUsecase;

    @QueryMapping
    public List<Student> findAllStudents() {
        return studentUsecase.findAllStudents();
    }

    @QueryMapping
    public Student findStudentById(@Argument Long id) {
        return studentUsecase.findStudentById(id).get();
    }

    @QueryMapping
    public List<Course> findAllCourses() {
        return courseUsecase.findAllCourses();
    }

    @QueryMapping
    public Course findCourseById(@Argument Long id) {
        return courseUsecase.findCourseById(id).get();
    }

    @QueryMapping
    public List<Enrollment> findAllEnrollments() {
        return enrollmentUsecase.findAllEnrollments();
    }

    @QueryMapping
    public Enrollment findEnrollmentById(@Argument Long id) {
        return enrollmentUsecase.findEnrollmentById(id).get();
    }

}
