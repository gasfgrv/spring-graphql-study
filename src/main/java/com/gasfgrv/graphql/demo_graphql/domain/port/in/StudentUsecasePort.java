package com.gasfgrv.graphql.demo_graphql.domain.port.in;

import java.util.List;
import java.util.Optional;

import com.gasfgrv.graphql.demo_graphql.domain.model.Student;

public interface StudentUsecasePort {

    List<Student> findAllStudents();

    Optional<Student> findStudentById(long id);

    List<Student> findAllStudentsByIds(List<Long> ids);

    Student createStudent(String name, String email);

    Student updateStudent(long id, String name, String email);

    boolean deleteStudent(long id);

}
