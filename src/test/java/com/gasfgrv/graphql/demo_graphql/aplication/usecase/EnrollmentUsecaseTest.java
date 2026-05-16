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

import com.gasfgrv.graphql.demo_graphql.aplication.exceptions.EnrollmentNotFoundException;
import com.gasfgrv.graphql.demo_graphql.domain.model.Enrollment;
import com.gasfgrv.graphql.demo_graphql.domain.port.out.CourseRepositoryPort;
import com.gasfgrv.graphql.demo_graphql.domain.port.out.EnrollmentRepositoryPort;
import com.gasfgrv.graphql.demo_graphql.domain.port.out.StudentRepositoryPort;
import com.gasfgrv.graphql.demo_graphql.mock.DataMock;

@ExtendWith(MockitoExtension.class)
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
    public void testFindAllEnrollments() {
        Enrollment enrollment = DataMock.createEnrollmentMock();

        when(enrollmentRepository.findAll()).thenReturn(List.of(enrollment));

        List<Enrollment> result = usecase.findAllEnrollments();

        assertThat(result).isNotNull()
                .hasSize(1)
                .contains(enrollment);

        verify(enrollmentRepository).findAll();
    }

    @Test
    public void testFindEnrollmentById() {
        Enrollment enrollment = DataMock.createEnrollmentMock();

        when(enrollmentRepository.findById(enrollment.getId())).thenReturn(Optional.of(enrollment));

        Enrollment result = usecase.findEnrollmentById(enrollment.getId()).orElseThrow();

        assertThat(result).isNotNull()
                .isEqualTo(enrollment);

        verify(enrollmentRepository).findById(enrollment.getId());
    }

    @Test
    public void testFindEnrollmentByIdNotFound() {
        long enrollmentId = 1L;

        when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usecase.findEnrollmentById(enrollmentId))
                .isInstanceOf(EnrollmentNotFoundException.class)
                .hasMessageContaining("Enrollment not found");

        verify(enrollmentRepository).findById(enrollmentId);
    }

    @Test
    public void testFindAllEnrollmentsByStudentsIds() {
        Enrollment enrollment = DataMock.createEnrollmentMock();

        when(enrollmentRepository.findAllByStudentIds(List.of(enrollment.getStudent().getId())))
                .thenReturn(List.of(enrollment));

        List<Enrollment> result = usecase
                .findAllEnrollmentsByStudentIds(List.of(enrollment.getStudent().getId()));

        assertThat(result).isNotNull()
                .hasSize(1)
                .contains(enrollment);

        verify(enrollmentRepository).findAllByStudentIds(List.of(enrollment.getStudent().getId()));
    }

    @Test
    public void testFindAllEnrollmentsByCourseIds() {
        Enrollment enrollment = DataMock.createEnrollmentMock();

        when(enrollmentRepository.findAllByCourseIds(List.of(enrollment.getCourse().getId())))
                .thenReturn(List.of(enrollment));

        List<Enrollment> result = usecase
                .findAllEnrollmentsByCourseIds(List.of(enrollment.getCourse().getId()));

        assertThat(result).isNotNull()
                .hasSize(1)
                .contains(enrollment);

        verify(enrollmentRepository).findAllByCourseIds(List.of(enrollment.getCourse().getId()));
    }

    @Test
    public void testCreateEnrollment() {
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

        verify(studentRepository).findById(enrollment.getStudent().getId());
        verify(courseRepository).findById(enrollment.getCourse().getId());
        verify(enrollmentRepository).save(any(Enrollment.class));
    }

    @Test
    public void testCreateEnrollmentStudentNotFound() {
        long studentId = 1L;
        long courseId = 2L;

        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usecase.createEnrollment(studentId, courseId))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Please provide a student for enrollment");

        verify(studentRepository).findById(studentId);
        verify(courseRepository).findById(courseId);
        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }

    @Test
    public void testCreateEnrollmentCourseNotFound() {
        Enrollment enrollment = DataMock.createEnrollmentMock();

        when(studentRepository.findById(enrollment.getStudent().getId()))
                .thenReturn(Optional.of(enrollment.getStudent()));
        when(courseRepository.findById(enrollment.getCourse().getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usecase.createEnrollment(enrollment.getStudent().getId(),
                enrollment.getCourse().getId()))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Please provide a course for enrollment");

        verify(studentRepository).findById(enrollment.getStudent().getId());
        verify(courseRepository).findById(enrollment.getCourse().getId());
        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }

    @Test
    public void testUpdateEnrollment() {
        Enrollment enrollment = DataMock.createEnrollmentMock();
        int newProgress = 50;

        when(enrollmentRepository.findById(enrollment.getId())).thenReturn(Optional.of(enrollment));
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);

        Enrollment result = usecase.updateEnrollment(enrollment.getId(), newProgress);

        assertThat(result).isNotNull()
                .isEqualTo(enrollment);

        verify(enrollmentRepository).findById(enrollment.getId());
        verify(enrollmentRepository).save(any(Enrollment.class));
    }

    @Test
    public void testUpdateEnrollmentNotFound() {
        long enrollmentId = 1L;
        when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usecase.updateEnrollment(enrollmentId, 50))
                .isInstanceOf(EnrollmentNotFoundException.class);

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
