package com.gasfgrv.graphql.demo_graphql.mock;

import java.util.Collections;

import org.instancio.Instancio;
import org.instancio.Select;

import com.gasfgrv.graphql.demo_graphql.domain.model.Course;
import com.gasfgrv.graphql.demo_graphql.domain.model.Enrollment;
import com.gasfgrv.graphql.demo_graphql.domain.model.Level;
import com.gasfgrv.graphql.demo_graphql.domain.model.Student;
import com.gasfgrv.graphql.demo_graphql.infrastructure.adapter.course.CourseEntity;
import com.gasfgrv.graphql.demo_graphql.infrastructure.adapter.enrollment.EnrollmentEntity;
import com.gasfgrv.graphql.demo_graphql.infrastructure.adapter.student.StudentEntity;

public class DataMock {

    public static Student createStudentMock() {
        return Student.builder()
                .id(Instancio.gen().longs().range(1L, 1000L).get())
                .name("Jhon Doe")
                .email(Instancio.gen().text().pattern("john.doe@example.com").get())
                .createdAt(Instancio.gen().temporal().localDateTime().past().get())
                .enrollments(Collections.emptyList())
                .build();
    }

    public static Student createStudentMock(Long id) {
        return Student.builder()
                .id(id)
                .name("Jhon Doe")
                .email(Instancio.gen().text().pattern("john.doe@example.com").get())
                .createdAt(Instancio.gen().temporal().localDateTime().past().get())
                .enrollments(Collections.emptyList())
                .build();
    }

    public static Course createCourseMock() {
        return Course.builder()
                .id(Instancio.gen().longs().range(1L, 1000L).get())
                .title("Introduction to GraphQL")
                .description("Learn the basics of GraphQL")
                .level(Instancio.gen().enumOf(Level.class).get())
                .enrollments(Collections.emptyList())
                .build();
    }

    public static Course createCourseMock(Long id) {
        return Course.builder()
                .id(id)
                .title("Introduction to GraphQL")
                .description("Learn the basics of GraphQL")
                .level(Instancio.gen().enumOf(Level.class).get())
                .enrollments(Collections.emptyList())
                .build();
    }

    public static Enrollment createEnrollmentMock() {
        return Enrollment.builder()
                .id(Instancio.gen().longs().range(1L, 1000L).get())
                .student(createStudentMock())
                .course(createCourseMock())
                .progress(Instancio.gen().ints().range(1, 100).get())
                .enrolledAt(Instancio.gen().temporal().localDateTime().past().get())
                .build();
    }

    public static Enrollment createEnrollmentMock(Long id) {
        return Enrollment.builder()
                .id(id)
                .student(createStudentMock(id))
                .course(createCourseMock(id))
                .progress(Instancio.gen().ints().range(1, 100).get())
                .enrolledAt(Instancio.gen().temporal().localDateTime().past().get())
                .build();
    }

    public static StudentEntity createStudentEntityMock() {
        return Instancio.of(StudentEntity.class)
                .ignore(Select.field(StudentEntity::getEnrollments))
                .create();
    }

    public static CourseEntity createCourseEntityMock() {
        return Instancio.of(CourseEntity.class)
                .ignore(Select.field(CourseEntity::getEnrollments))
                .create();
    }

    public static EnrollmentEntity createEnrollmentEntityMock() {
        return Instancio.of(EnrollmentEntity.class)
                .supply(Select.field(EnrollmentEntity::getStudent), DataMock::createStudentEntityMock)
                .supply(Select.field(EnrollmentEntity::getCourse), DataMock::createCourseEntityMock)
                .create();
    }

}
