package com.gasfgrv.graphql.demo_graphql.aplication.usecase;

import java.util.List;
import java.util.Optional;

import com.gasfgrv.graphql.demo_graphql.aplication.exceptions.StudentNotFoundException;
import com.gasfgrv.graphql.demo_graphql.domain.model.Student;
import com.gasfgrv.graphql.demo_graphql.domain.port.in.StudentUsecasePort;
import com.gasfgrv.graphql.demo_graphql.domain.port.out.StudentRepositoryPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class StudentUsecase implements StudentUsecasePort {

    private final StudentRepositoryPort repository;

    @Override
    public List<Student> findAllStudents() {
        log.info("Usecase: finding all students");
        return repository.findAll();
    }

    @Override
    public Optional<Student> findStudentById(long id) {
        log.info("Usecase: finding student by ID: {}", id);
        return Optional.of(repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Student with ID {} not found", id);
                    return new StudentNotFoundException();
                }));
    }

    @Override
    public List<Student> findAllStudentsByIds(List<Long> ids) {
        log.info("Usecase: finding students by IDs: {}", ids);
        return repository.findAllByIds(ids);
    }

    @Override
    public Student createStudent(String name, String email) {
        log.info("Usecase: creating student with name: {}", name);
        Student student = new Student()
                .validateName(name)
                .validateEmail(email)
                .createStudent();
        return repository.save(student);
    }

    @Override
    public Student updateStudent(long id, String name, String email) {
        log.info("Usecase: updating student with ID: {}", id);
        Student updated = repository.findById(id)
                .map(student -> student.updateData(name, email))
                .orElseThrow(() -> {
                    log.warn("Student with ID {} not found for update", id);
                    return new StudentNotFoundException();
                });
        return repository.save(updated);
    }

    @Override
    public boolean deleteStudent(long id) {
        log.info("Usecase: deleting student with ID: {}", id);
        return repository.deleteById(id);
    }

}
