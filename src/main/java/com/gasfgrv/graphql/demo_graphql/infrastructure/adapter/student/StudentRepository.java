package com.gasfgrv.graphql.demo_graphql.infrastructure.adapter.student;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.gasfgrv.graphql.demo_graphql.domain.model.Student;
import com.gasfgrv.graphql.demo_graphql.domain.port.out.StudentRepositoryPort;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StudentRepository implements StudentRepositoryPort {

    private final StudentMapper mapper;
    private final StudentJpaRepository jpaRepository;

    @Override
    public List<Student> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Student> findById(long id) {
        return jpaRepository
                .findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Student save(Student student) {
        StudentEntity entity = mapper.toEntity(student);
        System.out.println(entity);
        StudentEntity save = jpaRepository.save(entity);
        System.out.println(save);
        Student domain = mapper.toDomain(save);
        System.out.println(domain);
        return domain;
    }

    @Override
    public boolean deleteById(long id) {
        Optional<StudentEntity> student = jpaRepository.findById(id);

        if (student.isEmpty()) {
            return false;
        }

        StudentEntity studentToBeDeleted = student.get();
        jpaRepository.deleteById(studentToBeDeleted.getId());
        return true;
    }

}
