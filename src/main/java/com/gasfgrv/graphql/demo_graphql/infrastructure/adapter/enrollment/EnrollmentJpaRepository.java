package com.gasfgrv.graphql.demo_graphql.infrastructure.adapter.enrollment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface EnrollmentJpaRepository extends JpaRepository<EnrollmentEntity, Long> {

    List<EnrollmentEntity> findAllByStudentIdIn(List<Long> studentIds);

    List<EnrollmentEntity> findAllByCourseIdIn(List<Long> courseIds);

}
