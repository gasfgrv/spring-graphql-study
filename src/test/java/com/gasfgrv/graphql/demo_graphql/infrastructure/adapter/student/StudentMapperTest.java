package com.gasfgrv.graphql.demo_graphql.infrastructure.adapter.student;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.test.util.ReflectionTestUtils;

import com.gasfgrv.graphql.demo_graphql.domain.model.Student;
import com.gasfgrv.graphql.demo_graphql.infrastructure.adapter.enrollment.EnrollmentMapper;
import com.gasfgrv.graphql.demo_graphql.mock.DataMock;

class StudentMapperTest {

    private StudentMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(StudentMapper.class);
        EnrollmentMapper enrollmentMapper = Mappers.getMapper(EnrollmentMapper.class);
        ReflectionTestUtils.setField(mapper, "enrollmentMapper", enrollmentMapper);
    }

    @Test
    void shouldMapStudentToStudentEntity() {
        Student student = DataMock.createStudentMock();
        StudentEntity entity = mapper.toEntity(student);

        assertThat(entity).isNotNull()
                .extracting(StudentEntity::getId, StudentEntity::getName, StudentEntity::getEmail,
                        StudentEntity::getCreatedAt)
                .containsExactly(student.getId(), student.getName(), student.getEmail(), student.getCreatedAt());

        assertThat(entity.getEnrollments()).isEmpty();
    }

    @Test
    void shouldMapStudentEntityToStudent() {
        StudentEntity entity = DataMock.createStudentEntityMock();
        Student student = mapper.toDomain(entity);

        assertThat(student).isNotNull()
                .extracting(Student::getId, Student::getName, Student::getEmail, Student::getCreatedAt)
                .containsExactly(entity.getId(), entity.getName(), entity.getEmail(), entity.getCreatedAt());

        assertThat(student.getEnrollments()).isNull();
    }

    @Test
    void shouldReturnNullWhenStudentIsNull() {
        assertThat(mapper.toEntity(null)).isNull();
    }

    @Test
    void shouldReturnNullWhenStudentEntityIsNull() {
        assertThat(mapper.toDomain(null)).isNull();
    }

}
