package com.gasfgrv.graphql.demo_graphql.infrastructure.resolver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.graphql.test.autoconfigure.GraphQlTest;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.gasfgrv.graphql.demo_graphql.aplication.exceptions.EnrollmentNotFoundException;
import com.gasfgrv.graphql.demo_graphql.domain.model.Course;
import com.gasfgrv.graphql.demo_graphql.domain.model.Enrollment;
import com.gasfgrv.graphql.demo_graphql.domain.model.Student;
import com.gasfgrv.graphql.demo_graphql.domain.port.in.CourseUsecasePort;
import com.gasfgrv.graphql.demo_graphql.domain.port.in.EnrollmentUsecasePort;
import com.gasfgrv.graphql.demo_graphql.domain.port.in.StudentUsecasePort;
import com.gasfgrv.graphql.demo_graphql.infrastructure.configuration.GraphQLConfig;
import com.gasfgrv.graphql.demo_graphql.mock.DataMock;

@GraphQlTest(EnrollmentResolver.class)
@Import(GraphQLConfig.class)
class EnrollmentResolverTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @MockitoBean
    private StudentUsecasePort studentUsecase;

    @MockitoBean
    private CourseUsecasePort courseUsecase;

    @MockitoBean
    private EnrollmentUsecasePort enrollmentUsecase;

    @Test
    void testFindAllEnrollments() {
        Enrollment enrollment = DataMock.createEnrollmentMock();

        when(enrollmentUsecase.findAllEnrollments()).thenReturn(List.of(enrollment));

        graphQlTester.document("""
                query {
                    findAllEnrollments {
                        id
                        progress
                    }
                }
                """)
                .execute()
                .path("findAllEnrollments")
                .entityList(Enrollment.class)
                .hasSize(1);
    }

    @Test
    void testFindEnrollmentById() {
        Enrollment enrollment = DataMock.createEnrollmentMock();

        when(enrollmentUsecase.findEnrollmentById(1L)).thenReturn(Optional.of(enrollment));

        graphQlTester.document("""
                query($id: ID!) {
                    findEnrollmentById(id: $id) {
                        id
                        progress
                    }
                }
                """)
                .variable("id", 1L)
                .execute()
                .path("findEnrollmentById")
                .entity(Enrollment.class)
                .satisfies(enrollmentResponse -> {
                    assertThat(enrollmentResponse)
                            .isNotNull();
                });
    }

    @Test
    void testFindEnrollmentByIdNotFound() {
        when(enrollmentUsecase.findEnrollmentById(1L))
                .thenThrow(new EnrollmentNotFoundException("Enrollment not found"));

        graphQlTester.document("""
                query($id: ID!) {
                    findEnrollmentById(id: $id) {
                        id
                        progress
                    }
                }
                """)
                .variable("id", 1L)
                .execute()
                .errors()
                .satisfy(errors -> {
                    assertThat(errors).hasSize(1);
                    assertThat(errors.get(0).getMessage()).isEqualTo("Enrollment not found");
                    assertThat(errors.get(0).getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
                });
    }

    @Test
    void testStudentBatchMapping() {
        Enrollment enrollment = DataMock.createEnrollmentMock();
        Student student = enrollment.getStudent();

        when(enrollmentUsecase.findAllEnrollments()).thenReturn(List.of(enrollment));
        when(studentUsecase.findAllStudentsByIds(List.of(student.getId()))).thenReturn(List.of(student));

        graphQlTester.document("""
                query {
                    findAllEnrollments {
                        id
                        student {
                            id
                            name
                            email
                        }
                    }
                }
                """)
                .execute()
                .path("findAllEnrollments[0].student")
                .entity(Student.class)
                .satisfies(studentResponse -> {
                    assertThat(studentResponse)
                            .isNotNull()
                            .extracting("name", "email")
                            .containsExactly(student.getName(), student.getEmail());
                });
    }

    @Test
    void testCourseBatchMapping() {
        Enrollment enrollment = DataMock.createEnrollmentMock();
        Course course = enrollment.getCourse();

        when(enrollmentUsecase.findAllEnrollments()).thenReturn(List.of(enrollment));
        when(courseUsecase.findAllCoursesByIds(List.of(course.getId()))).thenReturn(List.of(course));

        graphQlTester.document("""
                query {
                    findAllEnrollments {
                        id
                        course {
                            id
                            title
                            description
                        }
                    }
                }
                """)
                .execute()
                .path("findAllEnrollments[0].course")
                .entity(Course.class)
                .satisfies(courseResponse -> {
                    assertThat(courseResponse)
                            .isNotNull()
                            .extracting("title", "description")
                            .containsExactly(course.getTitle(), course.getDescription());
                });
    }

    @Test
    void testCreateEnrollment() {
        Enrollment enrollment = DataMock.createEnrollmentMock();
        Long studentId = enrollment.getStudent().getId();
        Long courseId = enrollment.getCourse().getId();

        when(enrollmentUsecase.createEnrollment(studentId, courseId)).thenReturn(enrollment);

        graphQlTester.document("""
                mutation($studentId: ID!, $courseId: ID!) {
                    createEnrollment(studentId: $studentId, courseId: $courseId) {
                        id
                        progress
                    }
                }
                """)
                .variable("studentId", studentId)
                .variable("courseId", courseId)
                .execute()
                .path("createEnrollment")
                .entity(Enrollment.class)
                .satisfies(enrollmentResponse -> {
                    assertThat(enrollmentResponse)
                            .isNotNull();
                });
    }

    @Test
    void testUpdateEnrollmentProgress() {
        Enrollment enrollment = DataMock.createEnrollmentMock();
        Long enrollmentId = enrollment.getId();
        int newProgress = 80;

        when(enrollmentUsecase.updateEnrollment(enrollmentId, newProgress)).thenReturn(enrollment);

        graphQlTester.document("""
                mutation($enrollmentId: ID!, $progress: Int!) {
                    updateEnrollmentProgress(id: $enrollmentId, progress: $progress) {
                        id
                        progress
                    }
                }
                """)
                .variable("enrollmentId", enrollmentId)
                .variable("progress", newProgress)
                .execute()
                .path("updateEnrollmentProgress")
                .entity(Enrollment.class)
                .satisfies(enrollmentResponse -> {
                    assertThat(enrollmentResponse)
                            .isNotNull();
                });
    }

}
