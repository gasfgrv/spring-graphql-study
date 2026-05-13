package com.gasfgrv.graphql.demo_graphql.infrastructure.adapter.enrollment;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.gasfgrv.graphql.demo_graphql.domain.model.Enrollment;
import com.gasfgrv.graphql.demo_graphql.domain.port.out.EnrollmentRepositoryPort;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class EnrollmentRepository implements EnrollmentRepositoryPort {

    private final EnrollmentMapper mapper;
    private final EnrollmentJpaRepository jpaRepository;

    @Override
    public List<Enrollment> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Enrollment> findById(long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Enrollment save(Enrollment enrollment) {
        EnrollmentEntity save = jpaRepository.save(mapper.toEntity(enrollment));
        return mapper.toDomain(save);
    }

}
