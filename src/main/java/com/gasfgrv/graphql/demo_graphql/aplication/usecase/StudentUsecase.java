package com.gasfgrv.graphql.demo_graphql.aplication.usecase;

import java.util.List;
import java.util.Optional;

import com.gasfgrv.graphql.demo_graphql.aplication.exceptions.StudentNotFoundException;
import com.gasfgrv.graphql.demo_graphql.domain.model.Student;
import com.gasfgrv.graphql.demo_graphql.domain.port.in.StudentUsecasePort;
import com.gasfgrv.graphql.demo_graphql.domain.port.out.StudentRepositoryPort;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StudentUsecase implements StudentUsecasePort {

    private final StudentRepositoryPort repository;

    @Override
    public List<Student> findAllStudents() {
        return repository.findAll();
    }

    @Override
    public Optional<Student> findStudentById(long id) {
        return Optional.of(repository.findById(id)
                .orElseThrow(StudentNotFoundException::new));
    }

    @Override
    public Student createStudent(String name, String email) {
        Student student = new Student()
                .validateName(name)
                .validateEmail(email)
                .createStudent();
        return repository.save(student);
    }

    @Override
    public Student updateStudent(long id, String name, String email) {
        Student updated = repository.findById(id)
                .map(student -> student.updateData(name, email))
                .orElseThrow(StudentNotFoundException::new);
        return repository.save(updated);
    }

    @Override
    public boolean deleteStudent(long id) {
        return repository.deleteById(id);
    }

}
