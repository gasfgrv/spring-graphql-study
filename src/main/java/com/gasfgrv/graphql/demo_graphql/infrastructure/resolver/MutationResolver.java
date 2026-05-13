package com.gasfgrv.graphql.demo_graphql.infrastructure.resolver;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
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
public class MutationResolver {

    private final StudentUsecasePort studentUsecase;
    private final CourseUsecasePort courseUsecase;
    private final EnrollmentUsecasePort enrollmentUsecase;

    @MutationMapping
    public Student createStudent(@Argument String name, @Argument String email) {
        return studentUsecase.createStudent(name, email);
    }

    @MutationMapping
    public Student updateStudent(@Argument Long id, @Argument String name, @Argument String email) {
        return studentUsecase.createStudent(name, email);
    }

    @MutationMapping
    public boolean deleteStudent(@Argument Long id) {
        return studentUsecase.deleteStudent(id);
    }

    @MutationMapping
    public Course createCourse(@Argument String title, @Argument String description, @Argument String level) {
        return courseUsecase.createCourse(title, description, level);
    }

    @MutationMapping
    public Course updateCourse(@Argument Long id, @Argument String title, @Argument String description,
            @Argument String level) {
        return courseUsecase.updateCourse(id, title, description, level);
    }

    @MutationMapping
    public boolean deleteCourse(@Argument Long id) {
        return courseUsecase.deleteCourse(id);
    }

    @MutationMapping
    public Enrollment createEnrollment(@Argument long studentId, @Argument long courseId) {
        return enrollmentUsecase.createEnrollment(studentId, courseId);
    }

    @MutationMapping
    public Enrollment updateEnrollmentProgress(@Argument Long id, @Argument int progress) {
        return enrollmentUsecase.updateEnrollment(id, progress);
    }

}
