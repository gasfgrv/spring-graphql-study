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

import com.gasfgrv.graphql.demo_graphql.aplication.exceptions.StudentNotFoundException;
import com.gasfgrv.graphql.demo_graphql.domain.model.Student;
import com.gasfgrv.graphql.demo_graphql.domain.port.out.StudentRepositoryPort;
import com.gasfgrv.graphql.demo_graphql.mock.DataMock;

@ExtendWith(MockitoExtension.class)
public class StudentUsecaseTest {

    @Mock
    private StudentRepositoryPort repository;

    @InjectMocks
    private StudentUsecase usecase;

    @Test
    public void testFindAllStudents() {
        Student student = DataMock.createStudentMock();

        when(repository.findAll()).thenReturn(List.of(student));

        List<Student> students = usecase.findAllStudents();

        assertThat(students).isNotNull()
                .hasSize(1)
                .contains(student);

        verify(repository).findAll();
    }

    @Test
    public void testFindStudentById() {
        Student student = DataMock.createStudentMock();

        when(repository.findById(student.getId())).thenReturn(Optional.of(student));

        Optional<Student> result = usecase.findStudentById(student.getId());

        assertThat(result).isPresent()
                .contains(student);

        verify(repository).findById(student.getId());
    }

    @Test
    public void testFindStudentByIdNotFound() {
        long studentId = 1L;
        when(repository.findById(studentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usecase.findStudentById(studentId))
                .isInstanceOf(StudentNotFoundException.class);

        verify(repository).findById(studentId);
    }

    @Test
    public void testFindAllStudentsByIds() {
        Student student = DataMock.createStudentMock();

        when(repository.findAllByIds(List.of(student.getId()))).thenReturn(List.of(student));

        List<Student> students = usecase.findAllStudentsByIds(List.of(student.getId()));

        assertThat(students).isNotNull()
                .hasSize(1)
                .contains(student);

        verify(repository).findAllByIds(List.of(student.getId()));
    }

    @Test
    public void testCreateStudent() {
        Student student = DataMock.createStudentMock();

        when(repository.save(any(Student.class))).thenReturn(student);

        Student created = usecase.createStudent(student.getName(), student.getEmail());

        assertThat(created).isNotNull()
                .isEqualTo(student);

        verify(repository).save(any(Student.class));
    }

    @Test
    public void testUpdateStudent() {
        Student student = DataMock.createStudentMock();
        Student updatedStudent = new Student()
                .validateName("Updated Name")
                .validateEmail("updated@example.com");

        when(repository.findById(student.getId())).thenReturn(Optional.of(student));
        when(repository.save(any(Student.class))).thenReturn(updatedStudent);

        Student result = usecase.updateStudent(student.getId(),
                updatedStudent.getName(),
                updatedStudent.getEmail());

        assertThat(result).isNotNull()
                .isEqualTo(updatedStudent);

        verify(repository).findById(student.getId());
        verify(repository).save(any(Student.class));
    }

    @Test
    public void testUpdateStudentNotFound() {
        long studentId = 1L;
        String name = "Name";
        String email = "email@example.com";

        when(repository.findById(studentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usecase.updateStudent(studentId, name, email))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("Student not found");

        verify(repository).findById(studentId);
        verify(repository, never()).save(any(Student.class));
    }

    @Test
    public void testDeleteStudent() {
        long studentId = 1L;

        when(repository.deleteById(studentId)).thenReturn(true);

        boolean deleted = usecase.deleteStudent(studentId);

        assertThat(deleted).isTrue();

        verify(repository).deleteById(studentId);
    }

}