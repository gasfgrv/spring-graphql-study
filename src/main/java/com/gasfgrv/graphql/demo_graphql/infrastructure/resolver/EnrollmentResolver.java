package com.gasfgrv.graphql.demo_graphql.infrastructure.resolver;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.gasfgrv.graphql.demo_graphql.domain.model.Course;
import com.gasfgrv.graphql.demo_graphql.domain.model.Enrollment;
import com.gasfgrv.graphql.demo_graphql.domain.model.Student;
import com.gasfgrv.graphql.demo_graphql.domain.port.in.CourseUsecasePort;
import com.gasfgrv.graphql.demo_graphql.domain.port.in.EnrollmentUsecasePort;
import com.gasfgrv.graphql.demo_graphql.domain.port.in.StudentUsecasePort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class EnrollmentResolver {

        private final StudentUsecasePort studentUsecase;
        private final CourseUsecasePort courseUsecase;
        private final EnrollmentUsecasePort enrollmentUsecase;

        @QueryMapping
        public List<Enrollment> findAllEnrollments() {
                log.info("GraphQL Query: findAllEnrollments");
                return enrollmentUsecase.findAllEnrollments();
        }

        @QueryMapping
        public Enrollment findEnrollmentById(@Argument Long id) {
                log.info("GraphQL Query: findEnrollmentById with ID: {}", id);
                return enrollmentUsecase.findEnrollmentById(id).get();
        }

        @BatchMapping(field = "student", typeName = "Enrollment")
        public Map<Enrollment, Student> student(List<Enrollment> enrollments) {
                log.info("GraphQL BatchMapping: student for {} enrollments", enrollments.size());
                List<Long> studentIds = enrollments.stream()
                                .map(enrollment -> enrollment.getStudent().getId())
                                .distinct()
                                .toList();

                Map<Long, Student> students = studentUsecase.findAllStudentsByIds(studentIds)
                                .stream()
                                .collect(Collectors.toMap(Student::getId, student -> student));

                return enrollments.stream()
                                .collect(Collectors.toMap(
                                                enrollment -> enrollment,
                                                enrollment -> students.get(enrollment.getStudent().getId())));
        }

        @BatchMapping(field = "course", typeName = "Enrollment")
        public Map<Enrollment, Course> course(List<Enrollment> enrollments) {
                log.info("GraphQL BatchMapping: course for {} enrollments", enrollments.size());
                List<Long> courseIds = enrollments.stream()
                                .map(enrollment -> enrollment.getCourse().getId())
                                .distinct()
                                .toList();

                Map<Long, Course> courses = courseUsecase.findAllCoursesByIds(courseIds)
                                .stream()
                                .collect(Collectors.toMap(Course::getId, course -> course));

                return enrollments.stream()
                                .collect(Collectors.toMap(
                                                enrollment -> enrollment,
                                                enrollment -> courses.get(enrollment.getCourse().getId())));
        }

        @MutationMapping
        public Enrollment createEnrollment(@Argument long studentId, @Argument long courseId) {
                log.info("GraphQL Mutation: createEnrollment for student ID: {} and course ID: {}", studentId, courseId);
                return enrollmentUsecase.createEnrollment(studentId, courseId);
        }

        @MutationMapping
        public Enrollment updateEnrollmentProgress(@Argument Long id, @Argument int progress) {
                log.info("GraphQL Mutation: updateEnrollmentProgress for ID: {} with progress: {}", id, progress);
                return enrollmentUsecase.updateEnrollment(id, progress);
        }

}
