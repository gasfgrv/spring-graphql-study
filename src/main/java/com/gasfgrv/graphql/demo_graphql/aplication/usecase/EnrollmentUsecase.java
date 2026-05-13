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

@RequiredArgsConstructor
public class EnrollmentUsecase implements EnrollmentUsecasePort {

    private final EnrollmentRepositoryPort enrollmentRepository;
    private final StudentRepositoryPort studentRepository;
    private final CourseRepositoryPort courseRepository;

    @Override
    public List<Enrollment> findAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    @Override
    public Optional<Enrollment> findEnrollmentById(long id) {
        return Optional.of(enrollmentRepository.findById(id)
                .orElseThrow(EnrollmentNotFoundException::new));
    }

    @Override
    public Enrollment createEnrollment(long studentId, long courseId) {
        Student student = studentRepository.findById(studentId).orElse(null);
        Course course = courseRepository.findById(courseId).orElse(null);

        Enrollment enrollment = new Enrollment()
                .createNewEnrollment(student, course);

        return enrollmentRepository.save(enrollment);
    }

    @Override
    public Enrollment updateEnrollment(long id, int progress) {
        Enrollment updated = enrollmentRepository.findById(id)
                .map(enrollment -> enrollment.updateProgress(progress))
                .orElseThrow(EnrollmentNotFoundException::new);
        return enrollmentRepository.save(updated);
    }

}
