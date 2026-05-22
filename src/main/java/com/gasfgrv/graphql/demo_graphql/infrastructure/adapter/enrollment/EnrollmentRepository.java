package com.gasfgrv.graphql.demo_graphql.infrastructure.adapter.enrollment;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.gasfgrv.graphql.demo_graphql.domain.model.Enrollment;
import com.gasfgrv.graphql.demo_graphql.domain.port.out.EnrollmentRepositoryPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class EnrollmentRepository implements EnrollmentRepositoryPort {

    private final EnrollmentMapper mapper;
    private final EnrollmentJpaRepository jpaRepository;

    @Override
    public List<Enrollment> findAll() {
        log.info("Finding all enrollments");
        return jpaRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Enrollment> findById(long id) {
        log.info("Finding enrollment by ID: {}", id);
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Enrollment> findAllByStudentIds(List<Long> studentIds) {
        log.info("Finding enrollments by student IDs: {}", studentIds);
        return jpaRepository.findAllByStudentIdIn(studentIds)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Enrollment> findAllByCourseIds(List<Long> courseIds) {
        log.info("Finding enrollments by course IDs: {}", courseIds);
        return jpaRepository.findAllByCourseIdIn(courseIds)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Enrollment save(Enrollment enrollment) {
        log.info("Saving enrollment: {}", enrollment);
        EnrollmentEntity save = jpaRepository.save(mapper.toEntity(enrollment));
        return mapper.toDomain(save);
    }

}
