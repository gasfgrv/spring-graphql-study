package com.gasfgrv.graphql.demo_graphql.infrastructure.adapter.course;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.Import;
import org.springframework.test.util.ReflectionTestUtils;

import com.gasfgrv.graphql.demo_graphql.TestcontainersConfiguration;
import com.gasfgrv.graphql.demo_graphql.domain.model.Course;
import com.gasfgrv.graphql.demo_graphql.infrastructure.adapter.enrollment.EnrollmentMapperImpl;
import com.gasfgrv.graphql.demo_graphql.mock.DataMock;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import({ TestcontainersConfiguration.class, CourseRepository.class, CourseMapperImpl.class,
        EnrollmentMapperImpl.class })
public class CourseRepositoryTest {

    @Autowired
    private CourseRepository adapter;

    @Autowired
    private CourseJpaRepository repository;

    @Test
    void testFindAll() {
        CourseEntity course01 = DataMock.createCourseJpaEntityMock();
        CourseEntity course02 = DataMock.createCourseJpaEntityMock();

        repository.saveAll(List.of(course01, course02));
        repository.flush();

        List<Course> courses = adapter.findAll();

        assertThat(courses).hasSize(2);
    }

    @Test
    void testFindById() {
        CourseEntity course = DataMock.createCourseJpaEntityMock();

        CourseEntity savedCourse = repository.save(course);
        repository.flush();

        Course foundCourse = adapter.findById(savedCourse.getId())
                .orElseGet(Assertions::fail);

        assertThat(foundCourse).isNotNull()
                .extracting(Course::getId,
                        Course::getTitle,
                        Course::getDescription,
                        Course::getLevel)
                .containsExactly(savedCourse.getId(),
                        savedCourse.getTitle(),
                        savedCourse.getDescription(),
                        savedCourse.getLevel());
    }

    @Test
    void testFindAllByIds() {
        CourseEntity course01 = DataMock.createCourseJpaEntityMock();
        CourseEntity course02 = DataMock.createCourseJpaEntityMock();

        CourseEntity savedCourse01 = repository.save(course01);
        CourseEntity savedCourse02 = repository.save(course02);
        repository.flush();

        List<Course> courses = adapter.findAllByIds(List.of(savedCourse01.getId(), savedCourse02.getId()));

        assertThat(courses).hasSize(2)
                .extracting(Course::getId)
                .containsExactlyInAnyOrder(savedCourse01.getId(), savedCourse02.getId());
    }

    @Test
    void testSave() {
        Course course = DataMock.createCourseMock();
        ReflectionTestUtils.setField(course, "id", null);

        Course savedCourse = adapter.save(course);

        assertThat(savedCourse).isNotNull()
                .extracting(Course::getId,
                        Course::getTitle,
                        Course::getDescription,
                        Course::getLevel)
                .containsExactly(savedCourse.getId(),
                        course.getTitle(),
                        course.getDescription(),
                        course.getLevel());
    }

    @Test
    void testDeleteById() {
        CourseEntity course = DataMock.createCourseJpaEntityMock();

        CourseEntity savedCourse = repository.save(course);
        repository.flush();

        boolean deleted = adapter.deleteById(savedCourse.getId());

        assertThat(deleted).isTrue();
        assertThat(repository.findById(savedCourse.getId())).isEmpty();
    }

    @Test
    void testDeleteByIdNotFound() {
        boolean deleted = adapter.deleteById(-1L);

        assertThat(deleted).isFalse();
    }

}
