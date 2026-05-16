package com.gasfgrv.graphql.demo_graphql.infrastructure.adapter.enrollment;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.gasfgrv.graphql.demo_graphql.domain.model.Enrollment;
import com.gasfgrv.graphql.demo_graphql.mock.DataMock;

class EnrollmentMapperTest {

    private EnrollmentMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(EnrollmentMapper.class);
    }

    @Test
    void shouldMapEnrollmentToEnrollmentEntity() {
        Enrollment enrollment = DataMock.createEnrollmentMock();
        EnrollmentEntity entity = mapper.toEntity(enrollment);

        assertThat(entity).isNotNull()
                .extracting(EnrollmentEntity::getId, EnrollmentEntity::getProgress, EnrollmentEntity::getEnrolledAt)
                .containsExactly(enrollment.getId(), enrollment.getProgress(), enrollment.getEnrolledAt());

        assertThat(entity.getStudent()).isNotNull()
                .extracting(student -> student.getId())
                .isEqualTo(enrollment.getStudent().getId());

        assertThat(entity.getCourse()).isNotNull()
                .extracting(course -> course.getId())
                .isEqualTo(enrollment.getCourse().getId());
    }

    @Test
    void shouldMapEnrollmentEntityToEnrollment() {
        EnrollmentEntity entity = DataMock.createEnrollmentEntityMock();
        Enrollment enrollment = mapper.toDomain(entity);

        assertThat(enrollment).isNotNull()
                .extracting(Enrollment::getId, Enrollment::getProgress, Enrollment::getEnrolledAt)
                .containsExactly(entity.getId(), entity.getProgress(), entity.getEnrolledAt());

        assertThat(enrollment.getStudent()).isNotNull()
                .extracting(student -> student.getId())
                .isEqualTo(entity.getStudent().getId());

        assertThat(enrollment.getCourse()).isNotNull()
                .extracting(course -> course.getId())
                .isEqualTo(entity.getCourse().getId());
    }

    @Test
    void shouldReturnNullWhenEnrollmentIsNull() {
        assertThat(mapper.toEntity(null)).isNull();
    }

    @Test
    void shouldReturnNullWhenEnrollmentEntityIsNull() {
        assertThat(mapper.toDomain(null)).isNull();
    }

}
