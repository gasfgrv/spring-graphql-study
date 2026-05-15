package com.gasfgrv.graphql.demo_graphql.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.gasfgrv.graphql.demo_graphql.domain.model.Student;

public interface StudentRepositoryPort {

    List<Student> findAll();

    Optional<Student> findById(long id);

    List<Student> findAllByIds(List<Long> ids);

    Student save(Student student);

    boolean deleteById(long id);

}
