package com.gasfgrv.graphql.demo_graphql.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.gasfgrv.graphql.demo_graphql.domain.model.Enrollment;

public interface EnrollmentRepositoryPort {

    List<Enrollment> findAll();

    Optional<Enrollment> findById(long id);

    Enrollment save(Enrollment enrollment);

}
