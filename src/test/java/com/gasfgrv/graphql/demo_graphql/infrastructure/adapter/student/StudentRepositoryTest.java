package com.gasfgrv.graphql.demo_graphql.infrastructure.adapter.student;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

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
import com.gasfgrv.graphql.demo_graphql.domain.model.Student;
import com.gasfgrv.graphql.demo_graphql.infrastructure.adapter.enrollment.EnrollmentMapperImpl;
import com.gasfgrv.graphql.demo_graphql.mock.DataMock;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ExtendWith(OutputCaptureExtension.class)
@Import({ TestcontainersConfiguration.class, StudentRepository.class, StudentMapperImpl.class,
        EnrollmentMapperImpl.class })
public class StudentRepositoryTest {

    @Autowired
    private StudentRepository adapter;

    @Autowired
    private StudentJpaRepository repository;

    @Test
    void testFindAll(CapturedOutput output) {
        StudentEntity student01 = DataMock.createStudentJpaEntityMock();
        StudentEntity student02 = DataMock.createStudentJpaEntityMock();

        repository.saveAll(List.of(student01, student02));
        repository.flush();

        List<Student> students = adapter.findAll();

        assertThat(students).hasSize(2);
        assertThat(output.getOut()).contains("Finding all students");
    }

    @Test
    void testFindById(CapturedOutput output) {
        StudentEntity student = DataMock.createStudentJpaEntityMock();

        StudentEntity savedStudent = repository.save(student);
        repository.flush();

        Student foundStudent = adapter.findById(savedStudent.getId())
                .orElseThrow();

        assertThat(foundStudent).isNotNull()
                .extracting(Student::getId,
                        Student::getName,
                        Student::getEmail)
                .containsExactly(savedStudent.getId(),
                        savedStudent.getName(),
                        savedStudent.getEmail());

        assertThat(output.getOut()).contains("Finding student by ID: " + savedStudent.getId());
    }

    @Test
    void testFindAllByIds(CapturedOutput output) {
        StudentEntity student01 = DataMock.createStudentJpaEntityMock();
        StudentEntity student02 = DataMock.createStudentJpaEntityMock();

        repository.saveAll(List.of(student01, student02));
        repository.flush();

        List<Student> students = adapter.findAllByIds(List.of(student01.getId(), student02.getId()));

        assertThat(students).hasSize(2);
        assertThat(output.getOut()).contains("Finding students by IDs: [" + student01.getId() + ", " + student02.getId() + "]");
    }

    @Test
    void testSave(CapturedOutput output) {
        Student student = DataMock.createStudentMock();
        ReflectionTestUtils.setField(student, "id", null);

        Student savedStudent = adapter.save(student);

        assertThat(savedStudent).isNotNull()
                .extracting(Student::getId,
                        Student::getName,
                        Student::getEmail)
                .containsExactly(savedStudent.getId(),
                        savedStudent.getName(),
                        savedStudent.getEmail());

        assertThat(output.getOut()).contains("Saving student: " + student);
    }

    @Test
    void testDeleteById(CapturedOutput output) {
        StudentEntity student = DataMock.createStudentJpaEntityMock();

        StudentEntity savedStudent = repository.save(student);
        repository.flush();

        boolean deleted = adapter.deleteById(savedStudent.getId());

        assertThat(deleted).isTrue();
        assertThat(output.getOut()).contains("Deleting student by ID: " + savedStudent.getId());
    }

    @Test
    void testDeleteByIdNotFound(CapturedOutput output) {
        long id = 999L;
        boolean deleted = adapter.deleteById(id);

        assertThat(deleted).isFalse();
        assertThat(output.getOut()).contains("Student with ID " + id + " not found, cannot delete");
    }

}
