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

import com.gasfgrv.graphql.demo_graphql.aplication.exceptions.CourseNotFoundException;
import com.gasfgrv.graphql.demo_graphql.domain.model.Course;
import com.gasfgrv.graphql.demo_graphql.domain.model.Enrollment;
import com.gasfgrv.graphql.demo_graphql.domain.port.in.CourseUsecasePort;
import com.gasfgrv.graphql.demo_graphql.domain.port.in.EnrollmentUsecasePort;
import com.gasfgrv.graphql.demo_graphql.infrastructure.configuration.GraphQLConfig;
import com.gasfgrv.graphql.demo_graphql.mock.DataMock;

@GraphQlTest(CourseResolver.class)
@Import(GraphQLConfig.class)
@ExtendWith(OutputCaptureExtension.class)
class CourseResolverTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @MockitoBean
    private CourseUsecasePort courseUsecase;

    @MockitoBean
    private EnrollmentUsecasePort enrollmentUsecase;

    @Test
    void testFindAllCourses(CapturedOutput output) {
        Course course = DataMock.createCourseMock();

        when(courseUsecase.findAllCourses()).thenReturn(List.of(course));

        graphQlTester.document("""
                query {
                    findAllCourses {
                        id
                        title
                        description
                        level
                    }
                }
                """)
                .execute()
                .path("findAllCourses")
                .entityList(Course.class)
                .hasSize(1);

        assertThat(output.getOut()).contains("GraphQL Query: findAllCourses");
    }

    @Test
    void testFindCourseById(CapturedOutput output) {
        long id = 1L;
        Course course = DataMock.createCourseMock();

        when(courseUsecase.findCourseById(id)).thenReturn(Optional.of(course));

        graphQlTester.document("""
                query($id: ID!) {
                    findCourseById(id: $id) {
                        id
                        title
                        description
                        level
                    }
                }
                """)
                .variable("id", id)
                .execute()
                .path("findCourseById")
                .entity(Course.class)
                .satisfies(courseResponse -> {
                    assertThat(courseResponse)
                            .isNotNull()
                            .extracting("title", "description")
                            .containsExactly("Introduction to GraphQL", "Learn the basics of GraphQL");
                });

        assertThat(output.getOut()).contains("GraphQL Query: findCourseById with ID: " + id);
    }

    @Test
    void testFindCourseByIdNotFound(CapturedOutput output) {
        String message = "Course not found";
        when(courseUsecase.findCourseById(1L)).thenThrow(new CourseNotFoundException(message));

        graphQlTester.document("""
                query($id: ID!) {
                    findCourseById(id: $id) {
                        id
                        title
                    }
                }
                """)
                .variable("id", 1L)
                .execute()
                .errors()
                .satisfy(errors -> {
                    assertThat(errors).hasSize(1);
                    assertThat(errors.get(0).getMessage()).isEqualTo(message);
                    assertThat(errors.get(0).getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
                });

        assertThat(output.getOut()).contains("Resource not found: " + message);
    }

    @Test
    void testEnrollmentsBatchMapping(CapturedOutput output) {
        Course mockCourse = DataMock.createCourseMock(1L);
        Enrollment enrollment = DataMock.createEnrollmentMock(1L);

        when(courseUsecase.findCourseById(anyLong())).thenReturn(Optional.of(mockCourse));
        when(enrollmentUsecase.findAllEnrollmentsByCourseIds(anyList())).thenReturn(List.of(enrollment));

        graphQlTester.document("""
                query($id: ID!) {
                    findCourseById(id: $id) {
                        id
                        title
                        enrollments {
                            id
                            progress
                            student {
                                id
                                name
                            }
                        }
                    }
                }
                """)
                .variable("id", 1L)
                .execute()
                .path("findCourseById.enrollments")
                .entityList(Enrollment.class)
                .hasSize(1);

        assertThat(output.getOut()).contains("GraphQL BatchMapping: enrollments for 1 courses");
    }

    @Test
    void testCreateCourse(CapturedOutput output) {
        Course course = DataMock.createCourseMock();
        String title = "New Course";

        when(courseUsecase.createCourse(anyString(), anyString(), anyString())).thenReturn(course);

        graphQlTester.document("""
                mutation($title: String!, $description: String, $level: String) {
                    createCourse(title: $title, description: $description, level: $level) {
                        id
                        title
                        description
                        level
                    }
                }
                """)
                .variable("title", title)
                .variable("description", "New Description")
                .variable("level", "Avançado")
                .execute()
                .path("createCourse")
                .entity(Course.class)
                .satisfies(courseResponse -> {
                    assertThat(courseResponse)
                            .isNotNull()
                            .extracting("title", "description")
                            .containsExactly("Introduction to GraphQL", "Learn the basics of GraphQL");
                });

        assertThat(output.getOut()).contains("GraphQL Mutation: createCourse with title: " + title);
    }

    @Test
    void testUpdateCourse(CapturedOutput output) {
        Course course = DataMock.createCourseMock();
        long id = 1L;

        when(courseUsecase.updateCourse(anyLong(), anyString(), anyString(), anyString())).thenReturn(course);

        graphQlTester.document("""
                mutation($id: ID!, $title: String, $description: String, $level: String) {
                    updateCourse(id: $id, title: $title, description: $description, level: $level) {
                        id
                        title
                        description
                        level
                    }
                }
                """)
                .variable("id", id)
                .variable("title", "Updated Course")
                .variable("description", "Updated Description")
                .variable("level", "Avançado")
                .execute()
                .path("updateCourse")
                .entity(Course.class)
                .satisfies(courseResponse -> {
                    assertThat(courseResponse)
                            .isNotNull()
                            .extracting("title", "description")
                            .containsExactly("Introduction to GraphQL", "Learn the basics of GraphQL");
                });

        assertThat(output.getOut()).contains("GraphQL Mutation: updateCourse with ID: " + id);
    }

    @Test
    void testDeleteCourse(CapturedOutput output) {
        long id = 1L;
        when(courseUsecase.deleteCourse(anyLong())).thenReturn(true);

        graphQlTester.document("""
                mutation($id: ID!) {
                    deleteCourse(id: $id)
                }
                """)
                .variable("id", id)
                .execute()
                .path("deleteCourse")
                .entity(Boolean.class)
                .isEqualTo(true);

        assertThat(output.getOut()).contains("GraphQL Mutation: deleteCourse with ID: " + id);
    }

}
