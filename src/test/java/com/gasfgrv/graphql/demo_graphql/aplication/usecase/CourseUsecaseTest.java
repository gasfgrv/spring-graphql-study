package com.gasfgrv.graphql.demo_graphql.aplication.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import com.gasfgrv.graphql.demo_graphql.aplication.exceptions.CourseNotFoundException;
import com.gasfgrv.graphql.demo_graphql.domain.model.Course;
import com.gasfgrv.graphql.demo_graphql.domain.port.out.CourseRepositoryPort;
import com.gasfgrv.graphql.demo_graphql.mock.DataMock;

@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
public class CourseUsecaseTest {

    @Mock
    private CourseRepositoryPort repository;

    @InjectMocks
    private CourseUsecase usecase;

    @Test
    public void testFindAllCourses(CapturedOutput output) {
        Course course = DataMock.createCourseMock();

        when(repository.findAll()).thenReturn(List.of(course));

        List<Course> courses = usecase.findAllCourses();

        assertThat(courses).isNotNull()
                .hasSize(1)
                .contains(course);

        assertThat(output.getOut()).contains("Usecase: finding all courses");

        verify(repository).findAll();
    }

    @Test
    public void testFindCourseById(CapturedOutput output) {
        long courseId = 1L;
        Course course = DataMock.createCourseMock();

        when(repository.findById(courseId)).thenReturn(Optional.of(course));

        Optional<Course> result = usecase.findCourseById(courseId);

        assertThat(result).isPresent()
                .contains(course);

        assertThat(output.getOut()).contains("Usecase: finding course by ID: " + courseId);

        verify(repository).findById(courseId);
    }

    @Test
    public void testFindCourseByIdNotFound(CapturedOutput output) {
        long courseId = 1L;

        when(repository.findById(courseId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usecase.findCourseById(courseId))
                .isInstanceOf(CourseNotFoundException.class);

        assertThat(output.getOut()).contains("Course with ID " + courseId + " not found");

        verify(repository).findById(courseId);
    }

    @Test
    public void testFindAllCoursesByIds(CapturedOutput output) {
        long courseId = 1L;
        Course course = DataMock.createCourseMock();

        when(repository.findAllByIds(List.of(courseId))).thenReturn(List.of(course));

        List<Course> courses = usecase.findAllCoursesByIds(List.of(courseId));

        assertThat(courses).isNotNull()
                .hasSize(1)
                .contains(course);

        assertThat(output.getOut()).contains("Usecase: finding courses by IDs: [" + courseId + "]");

        verify(repository).findAllByIds(List.of(courseId));
    }

    @Test
    public void testCreateCourse(CapturedOutput output) {
        Course course = DataMock.createCourseMock();

        when(repository.save(any(Course.class))).thenReturn(course);

        Course created = usecase.createCourse(course.getTitle(),
                course.getDescription(),
                course.getLevel().getLevel());

        assertThat(created).isNotNull()
                .isEqualTo(course);

        assertThat(output.getOut()).contains("Usecase: creating course with title: " + course.getTitle());

        verify(repository).save(any(Course.class));
    }

    @Test
    public void testUpdateCourse(CapturedOutput output) {
        long courseId = 1L;
        String newTitle = "New Title";
        String newDescription = "New Description";
        String level = "Avançado";

        Course course = DataMock.createCourseMock();
        Course updatedCourse = course.updateData(newTitle, newDescription, level);

        when(repository.findById(courseId)).thenReturn(Optional.of(course));
        when(repository.save(any(Course.class))).thenReturn(updatedCourse);

        Course result = usecase.updateCourse(courseId, newTitle, newDescription, level);
        assertThat(result).isNotNull()
                .isEqualTo(updatedCourse);

        assertThat(output.getOut()).contains("Usecase: updating course with ID: " + courseId);

        verify(repository).findById(courseId);
        verify(repository).save(any(Course.class));
    }

    @Test
    public void testUpdateCourseNotFound(CapturedOutput output) {
        long courseId = 1L;
        String newTitle = "New Title";
        String newDescription = "New Description";
        String level = "Avançado";

        when(repository.findById(courseId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usecase.updateCourse(courseId, newTitle, newDescription, level))
                .isInstanceOf(CourseNotFoundException.class)
                .hasMessageContaining("Course not found");

        assertThat(output.getOut()).contains("Course with ID " + courseId + " not found for update");

        verify(repository).findById(courseId);
        verify(repository, never()).save(any(Course.class));
    }

    @Test
    public void testDeleteCourse(CapturedOutput output) {
        long courseId = 1L;

        when(repository.deleteById(courseId)).thenReturn(true);

        boolean deleted = usecase.deleteCourse(courseId);

        assertThat(deleted).isTrue();

        assertThat(output.getOut()).contains("Usecase: deleting course with ID: " + courseId);

        verify(repository).deleteById(courseId);
    }

}
