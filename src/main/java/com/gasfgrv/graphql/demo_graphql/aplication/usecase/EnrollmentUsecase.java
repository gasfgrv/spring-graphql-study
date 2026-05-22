package com.gasfgrv.graphql.demo_graphql.aplication.usecase;

import java.util.List;
import java.util.Optional;

import com.gasfgrv.graphql.demo_graphql.aplication.exceptions.EnrollmentNotFoundException;
import com.gasfgrv.graphql.demo_graphql.domain.model.Course;
import com.gasfgrv.graphql.demo_graphql.domain.model.Enrollment;
import com.gasfgrv.graphql.demo_graphql.domain.model.Student;
import com.gasfgrv.graphql.demo_graphql.domain.port.in.EnrollmentUsecasePort;
import com.gasfgrv.graphql.demo_graphql.domain.port.out.CourseRepositoryPort;
import com.gasfgrv.graphql.demo_graphql.domain.port.out.EnrollmentRepositoryPort;
import com.gasfgrv.graphql.demo_graphql.domain.port.out.StudentRepositoryPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class EnrollmentUsecase implements EnrollmentUsecasePort {

    private final EnrollmentRepositoryPort enrollmentRepository;
    private final StudentRepositoryPort studentRepository;
    private final CourseRepositoryPort courseRepository;

    @Override
    public List<Enrollment> findAllEnrollments() {
        log.info("Usecase: finding all enrollments");
        return enrollmentRepository.findAll();
    }

    @Override
    public Optional<Enrollment> findEnrollmentById(long id) {
        log.info("Usecase: finding enrollment by ID: {}", id);
        return Optional.of(enrollmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Enrollment with ID {} not found", id);
                    return new EnrollmentNotFoundException();
                }));
    }

    @Override
    public List<Enrollment> findAllEnrollmentsByStudentIds(List<Long> studentIds) {
        log.info("Usecase: finding enrollments by student IDs: {}", studentIds);
        return enrollmentRepository.findAllByStudentIds(studentIds);
    }

    @Override
    public List<Enrollment> findAllEnrollmentsByCourseIds(List<Long> courseIds) {
        log.info("Usecase: finding enrollments by course IDs: {}", courseIds);
        return enrollmentRepository.findAllByCourseIds(courseIds);
    }

    @Override
    public Enrollment createEnrollment(long studentId, long courseId) {
        log.info("Usecase: creating enrollment for student ID: {} and course ID: {}", studentId, courseId);
        Student student = studentRepository.findById(studentId).orElse(null);
        Course course = courseRepository.findById(courseId).orElse(null);

        if (student == null) {
            log.warn("Student with ID {} not found while creating enrollment", studentId);
        }
        if (course == null) {
            log.warn("Course with ID {} not found while creating enrollment", courseId);
        }

        Enrollment enrollment = new Enrollment()
                .createNewEnrollment(student, course);

        return enrollmentRepository.save(enrollment);
    }

    @Override
    public Enrollment updateEnrollment(long id, int progress) {
        log.info("Usecase: updating enrollment progress for ID: {} to {}%", id, progress);
        Enrollment updated = enrollmentRepository.findById(id)
                .map(enrollment -> enrollment.updateProgress(progress))
                .orElseThrow(() -> {
                    log.warn("Enrollment with ID {} not found for update", id);
                    return new EnrollmentNotFoundException();
                });
        return enrollmentRepository.save(updated);
    }

}
