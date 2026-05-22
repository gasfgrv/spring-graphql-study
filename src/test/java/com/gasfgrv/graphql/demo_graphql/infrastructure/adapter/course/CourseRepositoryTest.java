package com.gasfgrv.graphql.demo_graphql.infrastructure.adapter.course;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.Import;
import org.springframework.test.util.ReflectionTestUtils;

import com.gasfgrv.graphql.demo_graphql.TestcontainersConfiguration;
import com.gasfgrv.graphql.demo_graphql.domain.model.Course;
import com.gasfgrv.graphql.demo_graphql.infrastructure.adapter.enrollment.EnrollmentMapperImpl;
import com.gasfgrv.graphql.demo_graphql.mock.DataMock;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ExtendWith(OutputCaptureExtension.class)
@Import({ TestcontainersConfiguration.class, CourseRepository.class, CourseMapperImpl.class,
        EnrollmentMapperImpl.class })
public class CourseRepositoryTest {

    @Autowired
    private CourseRepository adapter;

    @Autowired
    private CourseJpaRepository repository;

    @Test
    void testFindAll(CapturedOutput output) {
        CourseEntity course01 = DataMock.createCourseJpaEntityMock();
        CourseEntity course02 = DataMock.createCourseJpaEntityMock();

        repository.saveAll(List.of(course01, course02));
        repository.flush();

        List<Course> courses = adapter.findAll();

        assertThat(courses).hasSize(2);
        assertThat(output.getOut()).contains("Finding all courses");
    }

    @Test
    void testFindById(CapturedOutput output) {
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

        assertThat(output.getOut()).contains("Finding course by ID: " + savedCourse.getId());
    }

    @Test
    void testFindAllByIds(CapturedOutput output) {
        CourseEntity course01 = DataMock.createCourseJpaEntityMock();
        CourseEntity course02 = DataMock.createCourseJpaEntityMock();

        CourseEntity savedCourse01 = repository.save(course01);
        CourseEntity savedCourse02 = repository.save(course02);
        repository.flush();

        List<Course> courses = adapter.findAllByIds(List.of(savedCourse01.getId(), savedCourse02.getId()));

        assertThat(courses).hasSize(2)
                .extracting(Course::getId)
                .containsExactlyInAnyOrder(savedCourse01.getId(), savedCourse02.getId());

        assertThat(output.getOut()).contains("Finding courses by IDs: [" + savedCourse01.getId() + ", " + savedCourse02.getId() + "]");
    }

    @Test
    void testSave(CapturedOutput output) {
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

        assertThat(output.getOut()).contains("Saving course: " + course);
    }

    @Test
    void testDeleteById(CapturedOutput output) {
        CourseEntity course = DataMock.createCourseJpaEntityMock();

        CourseEntity savedCourse = repository.save(course);
        repository.flush();

        boolean deleted = adapter.deleteById(savedCourse.getId());

        assertThat(deleted).isTrue();
        assertThat(repository.findById(savedCourse.getId())).isEmpty();

        assertThat(output.getOut()).contains("Deleting course by ID: " + savedCourse.getId());
    }

    @Test
    void testDeleteByIdNotFound(CapturedOutput output) {
        long id = -1L;
        boolean deleted = adapter.deleteById(id);

        assertThat(deleted).isFalse();
        assertThat(output.getOut()).contains("Course with ID " + id + " not found, cannot delete");
    }

}
