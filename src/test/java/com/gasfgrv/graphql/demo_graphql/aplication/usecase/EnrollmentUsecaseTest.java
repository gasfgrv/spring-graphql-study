package com.gasfgrv.graphql.demo_graphql.aplication.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import com.gasfgrv.graphql.demo_graphql.aplication.exceptions.EnrollmentNotFoundException;
import com.gasfgrv.graphql.demo_graphql.domain.model.Enrollment;
import com.gasfgrv.graphql.demo_graphql.domain.port.out.CourseRepositoryPort;
import com.gasfgrv.graphql.demo_graphql.domain.port.out.EnrollmentRepositoryPort;
import com.gasfgrv.graphql.demo_graphql.domain.port.out.StudentRepositoryPort;
import com.gasfgrv.graphql.demo_graphql.mock.DataMock;

@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
public class EnrollmentUsecaseTest {

    @Mock
    private EnrollmentRepositoryPort enrollmentRepository;

    @Mock
    private StudentRepositoryPort studentRepository;

    @Mock
    private CourseRepositoryPort courseRepository;

    @InjectMocks
    private EnrollmentUsecase usecase;

    @Test
    public void testFindAllEnrollments(CapturedOutput output) {
        Enrollment enrollment = DataMock.createEnrollmentMock();

        when(enrollmentRepository.findAll()).thenReturn(List.of(enrollment));

        List<Enrollment> result = usecase.findAllEnrollments();

        assertThat(result).isNotNull()
                .hasSize(1)
                .contains(enrollment);

        assertThat(output.getOut()).contains("Usecase: finding all enrollments");

        verify(enrollmentRepository).findAll();
    }

    @Test
    public void testFindEnrollmentById(CapturedOutput output) {
        Enrollment enrollment = DataMock.createEnrollmentMock();

        when(enrollmentRepository.findById(enrollment.getId())).thenReturn(Optional.of(enrollment));

        Enrollment result = usecase.findEnrollmentById(enrollment.getId()).orElseThrow();

        assertThat(result).isNotNull()
                .isEqualTo(enrollment);

        assertThat(output.getOut()).contains("Usecase: finding enrollment by ID: " + enrollment.getId());

        verify(enrollmentRepository).findById(enrollment.getId());
    }

    @Test
    public void testFindEnrollmentByIdNotFound(CapturedOutput output) {
        long enrollmentId = 1L;

        when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usecase.findEnrollmentById(enrollmentId))
                .isInstanceOf(EnrollmentNotFoundException.class)
                .hasMessageContaining("Enrollment not found");

        assertThat(output.getOut()).contains("Enrollment with ID " + enrollmentId + " not found");

        verify(enrollmentRepository).findById(enrollmentId);
    }

    @Test
    public void testFindAllEnrollmentsByStudentsIds(CapturedOutput output) {
        Enrollment enrollment = DataMock.createEnrollmentMock();

        when(enrollmentRepository.findAllByStudentIds(List.of(enrollment.getStudent().getId())))
                .thenReturn(List.of(enrollment));

        List<Enrollment> result = usecase
                .findAllEnrollmentsByStudentIds(List.of(enrollment.getStudent().getId()));

        assertThat(result).isNotNull()
                .hasSize(1)
                .contains(enrollment);

        assertThat(output.getOut()).contains("Usecase: finding enrollments by student IDs: [" + enrollment.getStudent().getId() + "]");

        verify(enrollmentRepository).findAllByStudentIds(List.of(enrollment.getStudent().getId()));
    }

    @Test
    public void testFindAllEnrollmentsByCourseIds(CapturedOutput output) {
        Enrollment enrollment = DataMock.createEnrollmentMock();

        when(enrollmentRepository.findAllByCourseIds(List.of(enrollment.getCourse().getId())))
                .thenReturn(List.of(enrollment));

        List<Enrollment> result = usecase
                .findAllEnrollmentsByCourseIds(List.of(enrollment.getCourse().getId()));

        assertThat(result).isNotNull()
                .hasSize(1)
                .contains(enrollment);

        assertThat(output.getOut()).contains("Usecase: finding enrollments by course IDs: [" + enrollment.getCourse().getId() + "]");

        verify(enrollmentRepository).findAllByCourseIds(List.of(enrollment.getCourse().getId()));
    }

    @Test
    public void testCreateEnrollment(CapturedOutput output) {
        Enrollment enrollment = DataMock.createEnrollmentMock();

        when(studentRepository.findById(enrollment.getStudent().getId()))
                .thenReturn(Optional.of(enrollment.getStudent()));
        when(courseRepository.findById(enrollment.getCourse().getId()))
                .thenReturn(Optional.of(enrollment.getCourse()));
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);

        Enrollment result = usecase.createEnrollment(enrollment.getStudent().getId(),
                enrollment.getCourse().getId());

        assertThat(result).isNotNull()
                .isEqualTo(enrollment);

        assertThat(output.getOut()).contains("Usecase: creating enrollment for student ID: " + enrollment.getStudent().getId() + " and course ID: " + enrollment.getCourse().getId());

        verify(studentRepository).findById(enrollment.getStudent().getId());
        verify(courseRepository).findById(enrollment.getCourse().getId());
        verify(enrollmentRepository).save(any(Enrollment.class));
    }

    @Test
    public void testCreateEnrollmentStudentNotFound(CapturedOutput output) {
        long studentId = 1L;
        long courseId = 2L;

        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(DataMock.createCourseMock()));

        assertThatThrownBy(() -> usecase.createEnrollment(studentId, courseId))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Please provide a student for enrollment");

        assertThat(output.getOut()).contains("Student with ID " + studentId + " not found while creating enrollment");

        verify(studentRepository).findById(studentId);
        verify(courseRepository).findById(courseId);
        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }

    @Test
    public void testCreateEnrollmentCourseNotFound(CapturedOutput output) {
        Enrollment enrollment = DataMock.createEnrollmentMock();

        when(studentRepository.findById(enrollment.getStudent().getId()))
                .thenReturn(Optional.of(enrollment.getStudent()));
        when(courseRepository.findById(enrollment.getCourse().getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usecase.createEnrollment(enrollment.getStudent().getId(),
                enrollment.getCourse().getId()))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Please provide a course for enrollment");

        assertThat(output.getOut()).contains("Course with ID " + enrollment.getCourse().getId() + " not found while creating enrollment");

        verify(studentRepository).findById(enrollment.getStudent().getId());
        verify(courseRepository).findById(enrollment.getCourse().getId());
        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }

    @Test
    public void testUpdateEnrollment(CapturedOutput output) {
        Enrollment enrollment = DataMock.createEnrollmentMock();
        int newProgress = 50;

        when(enrollmentRepository.findById(enrollment.getId())).thenReturn(Optional.of(enrollment));
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);

        Enrollment result = usecase.updateEnrollment(enrollment.getId(), newProgress);

        assertThat(result).isNotNull()
                .isEqualTo(enrollment);

        assertThat(output.getOut()).contains("Usecase: updating enrollment progress for ID: " + enrollment.getId() + " to " + newProgress + "%");

        verify(enrollmentRepository).findById(enrollment.getId());
        verify(enrollmentRepository).save(any(Enrollment.class));
    }

    @Test
    public void testUpdateEnrollmentNotFound(CapturedOutput output) {
        long enrollmentId = 1L;
        when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usecase.updateEnrollment(enrollmentId, 50))
                .isInstanceOf(EnrollmentNotFoundException.class);

        assertThat(output.getOut()).contains("Enrollment with ID " + enrollmentId + " not found for update");

        verify(enrollmentRepository).findById(enrollmentId);
        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }

    @Test
    public void testUpdateEnrollmentInvalidProgress() {
        Enrollment enrollment = DataMock.createEnrollmentMock();

        when(enrollmentRepository.findById(enrollment.getId())).thenReturn(Optional.of(enrollment));

        assertThatThrownBy(() -> usecase.updateEnrollment(enrollment.getId(), 150))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The value of progress should be between 0 and 100");

        verify(enrollmentRepository).findById(enrollment.getId());
        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }

}
