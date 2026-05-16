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

import com.gasfgrv.graphql.demo_graphql.aplication.exceptions.CourseNotFoundException;
import com.gasfgrv.graphql.demo_graphql.domain.model.Course;
import com.gasfgrv.graphql.demo_graphql.domain.port.out.CourseRepositoryPort;
import com.gasfgrv.graphql.demo_graphql.mock.DataMock;

@ExtendWith(MockitoExtension.class)
public class CourseUsecaseTest {

    @Mock
    private CourseRepositoryPort repository;

    @InjectMocks
    private CourseUsecase usecase;

    @Test
    public void testFindAllCourses() {
        Course course = DataMock.createCourseMock();

        when(repository.findAll()).thenReturn(List.of(course));

        List<Course> courses = usecase.findAllCourses();

        assertThat(courses).isNotNull()
                .hasSize(1)
                .contains(course);

        verify(repository).findAll();
    }

    @Test
    public void testFindCourseById() {
        Course course = DataMock.createCourseMock();

        when(repository.findById(1L)).thenReturn(Optional.of(course));

        var result = usecase.findCourseById(1L);

        assertThat(result).isPresent()
                .contains(course);

        verify(repository).findById(1L);
    }

    @Test
    public void testFindCourseByIdNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usecase.findCourseById(1L))
                .isInstanceOf(CourseNotFoundException.class);

        verify(repository).findById(1L);
    }

    @Test
    public void testFindAllCoursesByIds() {
        Course course = DataMock.createCourseMock();

        when(repository.findAllByIds(List.of(1L))).thenReturn(List.of(course));

        List<Course> courses = usecase.findAllCoursesByIds(List.of(1L));

        assertThat(courses).isNotNull()
                .hasSize(1)
                .contains(course);

        verify(repository).findAllByIds(List.of(1L));
    }

    @Test
    public void testCreateCourse() {
        Course course = DataMock.createCourseMock();

        when(repository.save(any(Course.class))).thenReturn(course);

        Course created = usecase.createCourse(course.getTitle(), course.getDescription(), course.getLevel().getLevel());

        assertThat(created).isNotNull()
                .isEqualTo(course);

        verify(repository).save(any(Course.class));
    }

    @Test
    public void testUpdateCourse() {
        Course course = DataMock.createCourseMock();
        Course updatedCourse = course.updateData("New Title", "New Description", "Avançado");

        when(repository.findById(1L)).thenReturn(Optional.of(course));
        when(repository.save(any(Course.class))).thenReturn(updatedCourse);

        Course result = usecase.updateCourse(1L, "New Title", "New Description", "Avançado");

        assertThat(result).isNotNull()
                .isEqualTo(updatedCourse);

        verify(repository).findById(1L);
        verify(repository).save(any(Course.class));
    }

    @Test
    public void testUpdateCourseNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usecase.updateCourse(1L, "New Title", "New Description", "Avançado"))
                .isInstanceOf(CourseNotFoundException.class);

        verify(repository).findById(1L);
        verify(repository, never()).save(any(Course.class));
    }

    @Test
    public void testDeleteCourse() {
        when(repository.deleteById(1L)).thenReturn(true);

        boolean deleted = usecase.deleteCourse(1L);

        assertThat(deleted).isTrue();

        verify(repository).deleteById(1L);
    }

}
