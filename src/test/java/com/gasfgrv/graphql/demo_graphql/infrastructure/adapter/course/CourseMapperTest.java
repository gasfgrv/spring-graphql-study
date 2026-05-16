package com.gasfgrv.graphql.demo_graphql.infrastructure.adapter.course;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.test.util.ReflectionTestUtils;

import com.gasfgrv.graphql.demo_graphql.domain.model.Course;
import com.gasfgrv.graphql.demo_graphql.infrastructure.adapter.enrollment.EnrollmentMapper;
import com.gasfgrv.graphql.demo_graphql.mock.DataMock;

class CourseMapperTest {

    private CourseMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(CourseMapper.class);
        EnrollmentMapper enrollmentMapper = Mappers.getMapper(EnrollmentMapper.class);
        ReflectionTestUtils.setField(mapper, "enrollmentMapper", enrollmentMapper);
    }

    @Test
    void shouldMapCourseToCourseEntity() {
        Course course = DataMock.createCourseMock();
        CourseEntity entity = mapper.toEntity(course);

        assertThat(entity).isNotNull()
                .extracting(CourseEntity::getId,
                        CourseEntity::getTitle,
                        CourseEntity::getDescription,
                        CourseEntity::getLevel)
                .containsExactly(course.getId(),
                        course.getTitle(),
                        course.getDescription(),
                        course.getLevel());

        assertThat(entity.getEnrollments()).isEmpty();
    }

    @Test
    void shouldMapCourseEntityToCourse() {
        CourseEntity entity = DataMock.createCourseEntityMock();
        Course course = mapper.toDomain(entity);

        assertThat(course).isNotNull()
                .extracting(Course::getId,
                        Course::getTitle,
                        Course::getDescription,
                        Course::getLevel)
                .containsExactly(entity.getId(),
                        entity.getTitle(),
                        entity.getDescription(),
                        entity.getLevel());

        assertThat(course.getEnrollments()).isNull();
    }

    @Test
    void shouldReturnNullWhenCourseIsNull() {
        assertThat(mapper.toEntity(null)).isNull();
    }

    @Test
    void shouldReturnNullWhenCourseEntityIsNull() {
        assertThat(mapper.toDomain(null)).isNull();
    }

}
