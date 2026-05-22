package com.gasfgrv.graphql.demo_graphql.infrastructure.resolver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.graphql.test.autoconfigure.GraphQlTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.gasfgrv.graphql.demo_graphql.aplication.exceptions.StudentNotFoundException;
import com.gasfgrv.graphql.demo_graphql.domain.model.Enrollment;
import com.gasfgrv.graphql.demo_graphql.domain.model.Student;
import com.gasfgrv.graphql.demo_graphql.domain.port.in.EnrollmentUsecasePort;
import com.gasfgrv.graphql.demo_graphql.domain.port.in.StudentUsecasePort;
import com.gasfgrv.graphql.demo_graphql.infrastructure.configuration.GraphQLConfig;
import com.gasfgrv.graphql.demo_graphql.mock.DataMock;

@GraphQlTest(StudentResolver.class)
@Import(GraphQLConfig.class)
@ExtendWith(OutputCaptureExtension.class)
class StudentResolverTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @MockitoBean
    private StudentUsecasePort studentUsecase;

    @MockitoBean
    private EnrollmentUsecasePort enrollmentUsecase;

    @Test
    void testFindAllStudents(CapturedOutput output) {
        Student student = DataMock.createStudentMock();

        when(studentUsecase.findAllStudents()).thenReturn(List.of(student));

        graphQlTester.document("""
                query {
                    findAllStudents {
                        id
                        name
                        email
                    }
                }
                """)
                .execute()
                .path("findAllStudents")
                .entityList(Student.class)
                .hasSize(1);

        assertThat(output.getOut()).contains("GraphQL Query: findAllStudents");
    }

    @Test
    void testFindStudentById(CapturedOutput output) {
        long id = 1L;
        Student student = DataMock.createStudentMock();

        when(studentUsecase.findStudentById(id)).thenReturn(Optional.of(student));

        graphQlTester.document("""
                query($id: ID!) {
                    findStudentById(id: $id) {
                        id
                        name
                        email
                    }
                }
                """)
                .variable("id", id)
                .execute()
                .path("findStudentById")
                .entity(Student.class)
                .satisfies(studentResponse -> {
                    assertThat(studentResponse)
                            .isNotNull()
                            .extracting("name", "email")
                            .containsExactly("Jhon Doe", "john.doe@example.com");
                });

        assertThat(output.getOut()).contains("GraphQL Query: findStudentById with ID: " + id);
    }

    @Test
    void testFindStudentByIdNotFound() {
        when(studentUsecase.findStudentById(1L))
                .thenThrow(new StudentNotFoundException("Student not found"));

        graphQlTester.document("""
                query($id: ID!) {
                    findStudentById(id: $id) {
                        id
                        name
                    }
                }
                """)
                .variable("id", 1L)
                .execute()
                .errors()
                .satisfy(errors -> {
                    assertThat(errors).hasSize(1);
                    assertThat(errors.get(0).getMessage()).isEqualTo("Student not found");
                    assertThat(errors.get(0).getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
                });
    }

    @Test
    void testEnrollmentsBatchMapping(CapturedOutput output) {
        Student mockStudent = DataMock.createStudentMock(1L);
        Enrollment enrollment = DataMock.createEnrollmentMock(1L);

        when(studentUsecase.findStudentById(anyLong())).thenReturn(Optional.of(mockStudent));
        when(enrollmentUsecase.findAllEnrollmentsByStudentIds(anyList())).thenReturn(List.of(enrollment));

        graphQlTester.document("""
                query($id: ID!) {
                    findStudentById(id: $id) {
                        id
                        name
                        email
                        enrollments {
                            id
                            progress
                            course {
                                id
                                title
                            }
                        }
                    }
                }
                """)
                .variable("id", 1L)
                .execute()
                .path("findStudentById.enrollments")
                .entityList(Enrollment.class)
                .hasSize(1);

        assertThat(output.getOut()).contains("GraphQL BatchMapping: enrollments for 1 students");
    }

    @Test
    void testCreateStudent(CapturedOutput output) {
        Student student = DataMock.createStudentMock();
        String name = "Jhon Doe";

        when(studentUsecase.createStudent(anyString(), anyString())).thenReturn(student);

        graphQlTester.document("""
                mutation($name: String!, $email: String!) {
                    createStudent(name: $name, email: $email) {
                        id
                        name
                        email
                    }
                }
                """)
                .variable("name", name)
                .variable("email", "john.doe@example.com")
                .execute()
                .path("createStudent")
                .entity(Student.class)
                .satisfies(studentResponse -> {
                    assertThat(studentResponse)
                            .isNotNull()
                            .extracting("name", "email")
                            .containsExactly("Jhon Doe", "john.doe@example.com");
                });

        assertThat(output.getOut()).contains("GraphQL Mutation: createStudent with name: " + name);
    }

    @Test
    void testCreateStudentValidationError(CapturedOutput output) {
        String message = "Invalid email";
        when(studentUsecase.createStudent(anyString(), anyString()))
                .thenThrow(new IllegalArgumentException(message));

        graphQlTester.document("""
                mutation($name: String!, $email: String!) {
                    createStudent(name: $name, email: $email) {
                        id
                        name
                    }
                }
                """)
                .variable("name", "Jhon Doe")
                .variable("email", "invalid-email")
                .execute()
                .errors()
                .satisfy(errors -> {
                    assertThat(errors).hasSize(1);
                    assertThat(errors.get(0).getMessage()).isEqualTo(message);
                    assertThat(errors.get(0).getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
                });

        assertThat(output.getOut()).contains("Validation error: " + message);
    }

    @Test
    void testUpdateStudent(CapturedOutput output) {
        Student student = DataMock.createStudentMock();
        long id = 1L;

        when(studentUsecase.updateStudent(anyLong(), anyString(), anyString())).thenReturn(student);

        graphQlTester.document("""
                mutation($id: ID!, $name: String, $email: String) {
                    updateStudent(id: $id, name: $name, email: $email) {
                        id
                        name
                        email
                    }
                }
                """)
                .variable("id", id)
                .variable("name", "Jhon Doe")
                .variable("email", "john.doe@example.com")
                .execute()
                .path("updateStudent")
                .entity(Student.class)
                .satisfies(studentResponse -> {
                    assertThat(studentResponse)
                            .isNotNull()
                            .extracting("name", "email")
                            .containsExactly("Jhon Doe", "john.doe@example.com");
                });

        assertThat(output.getOut()).contains("GraphQL Mutation: updateStudent with ID: " + id);
    }

    @Test
    void testDeleteStudent(CapturedOutput output) {
        long id = 1L;
        when(studentUsecase.deleteStudent(anyLong())).thenReturn(true);

        graphQlTester.document("""
                mutation($id: ID!) {
                    deleteStudent(id: $id)
                }
                """)
                .variable("id", id)
                .execute()
                .path("deleteStudent")
                .entity(Boolean.class)
                .isEqualTo(true);

        assertThat(output.getOut()).contains("GraphQL Mutation: deleteStudent with ID: " + id);
    }
}
