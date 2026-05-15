package com.gasfgrv.graphql.demo_graphql.domain.port.in;

import java.util.List;
import java.util.Optional;

import com.gasfgrv.graphql.demo_graphql.domain.model.Enrollment;

public interface EnrollmentUsecasePort {

    List<Enrollment> findAllEnrollments();

    Optional<Enrollment> findEnrollmentById(long id);

    List<Enrollment> findAllEnrollmentsByStudentIds(List<Long> studentIds);

    List<Enrollment> findAllEnrollmentsByCourseIds(List<Long> courseIds);

    Enrollment createEnrollment(long studentId, long courseId);

    Enrollment updateEnrollment(long id, int progress);

}
