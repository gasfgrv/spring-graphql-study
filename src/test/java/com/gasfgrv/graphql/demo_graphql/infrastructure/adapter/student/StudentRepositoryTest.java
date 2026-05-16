package com.gasfgrv.graphql.demo_graphql.infrastructure.adapter.student;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.Import;
import org.springframework.test.util.ReflectionTestUtils;

import com.gasfgrv.graphql.demo_graphql.TestcontainersConfiguration;
import com.gasfgrv.graphql.demo_graphql.domain.model.Student;
import com.gasfgrv.graphql.demo_graphql.infrastructure.adapter.enrollment.EnrollmentMapperImpl;
import com.gasfgrv.graphql.demo_graphql.mock.DataMock;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import({ TestcontainersConfiguration.class, StudentRepository.class, StudentMapperImpl.class,
        EnrollmentMapperImpl.class })
public class StudentRepositoryTest {

    @Autowired
    private StudentRepository adapter;

    @Autowired
    private StudentJpaRepository repository;

    @Test
    void testFindAll() {
        StudentEntity student01 = DataMock.createStudentJpaEntityMock();
        StudentEntity student02 = DataMock.createStudentJpaEntityMock();

        repository.saveAll(List.of(student01, student02));
        repository.flush();

        List<Student> students = adapter.findAll();

        assertThat(students).hasSize(2);
    }

    @Test
    void testFindById() {
        StudentEntity student = DataMock.createStudentJpaEntityMock();

        StudentEntity savedStudent = repository.save(student);
        repository.flush();

        Student foundStudent = adapter.findById(savedStudent.getId())
                .orElseThrow();

        assertThat(foundStudent).isNotNull()
                .extracting(Student::getId, Student::getName, Student::getEmail)
                .containsExactly(savedStudent.getId(), savedStudent.getName(), savedStudent.getEmail());
    }

    @Test
    void testFindAllByIds() {
        StudentEntity student01 = DataMock.createStudentJpaEntityMock();
        StudentEntity student02 = DataMock.createStudentJpaEntityMock();

        repository.saveAll(List.of(student01, student02));
        repository.flush();

        List<Student> students = adapter.findAllByIds(List.of(student01.getId(), student02.getId()));

        assertThat(students).hasSize(2);
    }

    @Test
    void testSave() {
        Student student = DataMock.createStudentMock();
        ReflectionTestUtils.setField(student, "id", null);

        Student savedStudent = adapter.save(student);

        assertThat(savedStudent).isNotNull()
                .extracting(Student::getId, Student::getName, Student::getEmail)
                .containsExactly(savedStudent.getId(), savedStudent.getName(), savedStudent.getEmail());
    }

    @Test
    void testDeleteById() {
        StudentEntity student = DataMock.createStudentJpaEntityMock();

        StudentEntity savedStudent = repository.save(student);
        repository.flush();

        boolean deleted = adapter.deleteById(savedStudent.getId());

        assertThat(deleted).isTrue();
    }

    @Test
    void testDeleteByIdNotFound() {
        boolean deleted = adapter.deleteById(999L);
        assertThat(deleted).isFalse();
    }

}
