package com.gasfgrv.graphql.demo_graphql.infrastructure.adapter.enrollment;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace;
import org.springframework.context.annotation.Import;
import org.springframework.test.util.ReflectionTestUtils;

import com.gasfgrv.graphql.demo_graphql.TestcontainersConfiguration;
import com.gasfgrv.graphql.demo_graphql.domain.model.Enrollment;
import com.gasfgrv.graphql.demo_graphql.infrastructure.adapter.course.CourseEntity;
import com.gasfgrv.graphql.demo_graphql.infrastructure.adapter.course.CourseJpaRepository;
import com.gasfgrv.graphql.demo_graphql.infrastructure.adapter.student.StudentEntity;
import com.gasfgrv.graphql.demo_graphql.infrastructure.adapter.student.StudentJpaRepository;
import com.gasfgrv.graphql.demo_graphql.mock.DataMock;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import({ TestcontainersConfiguration.class, EnrollmentRepository.class, EnrollmentMapperImpl.class })
public class EnrollmentRepositoryTest {

    @Autowired
    private EnrollmentRepository adapter;

    @Autowired
    private EnrollmentJpaRepository enrollmentRepository;

    @Autowired
    private StudentJpaRepository studentRepository;

    @Autowired
    private CourseJpaRepository courseRepository;

    @Test
    void testFindAll() {
        EnrollmentEntity enrollment01 = DataMock.createEnrollmentJpaEntityMock();
        EnrollmentEntity enrollment02 = DataMock.createEnrollmentJpaEntityMock();

        List.of(enrollment01, enrollment02).forEach(enrollment -> {
            studentRepository.save(enrollment.getStudent());
            courseRepository.save(enrollment.getCourse());
        });

        enrollmentRepository.saveAll(List.of(enrollment01, enrollment02));
        enrollmentRepository.flush();

        List<Enrollment> enrollments = adapter.findAll();

        assertThat(enrollments).hasSize(2);
    }

    @Test
    void testFindById() {
        EnrollmentEntity enrollment = DataMock.createEnrollmentJpaEntityMock();

        studentRepository.save(enrollment.getStudent());
        courseRepository.save(enrollment.getCourse());

        EnrollmentEntity savedEnrollment = enrollmentRepository.save(enrollment);
        enrollmentRepository.flush();

        Enrollment foundEnrollment = adapter.findById(savedEnrollment.getId())
                .orElseThrow();

        assertThat(foundEnrollment).isNotNull()
                .extracting(Enrollment::getId,
                        enrollmentStudent -> enrollmentStudent.getStudent().getId(),
                        enrollmentCourse -> enrollmentCourse.getCourse().getId())
                .containsExactly(savedEnrollment.getId(),
                        savedEnrollment.getStudent().getId(),
                        savedEnrollment.getCourse().getId());
    }

    @Test
    void testFindAllByStudentIds() {
        EnrollmentEntity enrollment01 = DataMock.createEnrollmentJpaEntityMock();
        EnrollmentEntity enrollment02 = DataMock.createEnrollmentJpaEntityMock();

        List.of(enrollment01, enrollment02).forEach(enrollment -> {
            studentRepository.save(enrollment.getStudent());
            courseRepository.save(enrollment.getCourse());
        });

        enrollmentRepository.saveAll(List.of(enrollment01, enrollment02));
        enrollmentRepository.flush();

        List<Enrollment> enrollments = adapter.findAllByStudentIds(
                List.of(enrollment01.getStudent().getId(), enrollment02.getStudent().getId()));

        assertThat(enrollments).hasSize(2)
                .extracting(enrollmentStudent -> enrollmentStudent.getStudent().getId())
                .containsExactlyInAnyOrder(enrollment01.getStudent().getId(), enrollment02.getStudent().getId());
    }

    @Test
    void testFindAllByCourseIds() {
        EnrollmentEntity enrollment01 = DataMock.createEnrollmentJpaEntityMock();
        EnrollmentEntity enrollment02 = DataMock.createEnrollmentJpaEntityMock();

        List.of(enrollment01, enrollment02).forEach(enrollment -> {
            studentRepository.save(enrollment.getStudent());
            courseRepository.save(enrollment.getCourse());
        });

        enrollmentRepository.saveAll(List.of(enrollment01, enrollment02));
        enrollmentRepository.flush();

        List<Enrollment> enrollments = adapter.findAllByCourseIds(
                List.of(enrollment01.getCourse().getId(), enrollment02.getCourse().getId()));

        assertThat(enrollments).hasSize(2)
                .extracting(enrollmentCourse -> enrollmentCourse.getCourse().getId())
                .containsExactlyInAnyOrder(enrollment01.getCourse().getId(), enrollment02.getCourse().getId());
    }

    @Test
    void testSave() {
        Enrollment enrollment = DataMock.createEnrollmentMock();
        ReflectionTestUtils.setField(enrollment, "id", null);

        StudentEntity studentEntity = studentRepository.saveAndFlush(DataMock.createStudentJpaEntityMock());
        ReflectionTestUtils.setField(enrollment.getStudent(), "id", studentEntity.getId());

        CourseEntity courseEntity = courseRepository.saveAndFlush(DataMock.createCourseJpaEntityMock());
        ReflectionTestUtils.setField(enrollment.getCourse(), "id", courseEntity.getId());

        Enrollment savedEnrollment = adapter.save(enrollment);

        assertThat(savedEnrollment).isNotNull()
                .extracting(Enrollment::getId,
                        enrollmentStudent -> enrollmentStudent.getStudent().getId(),
                        enrollmentCourse -> enrollmentCourse.getCourse().getId())
                .containsExactly(savedEnrollment.getId(),
                        enrollment.getStudent().getId(),
                        enrollment.getCourse().getId());
    }

}
