package com.gasfgrv.graphql.demo_graphql.domain.model;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class EnrollmentTest {

    @Test
    void testCreateNewEnrollmentSuccess() {
        Enrollment enrollment = new Enrollment();
        Student student = Student.builder().id(1L).name("Gustavo").build();
        Course course = Course.builder().id(1L).title("Spring GraphQL").build();

        enrollment.createNewEnrollment(student, course);

        assertThat(enrollment.getStudent()).isEqualTo(student);
        assertThat(enrollment.getCourse()).isEqualTo(course);
        assertThat(enrollment.getProgress()).isZero();
        assertThat(enrollment.getEnrolledAt()).isCloseTo(LocalDateTime.now(), within(1, SECONDS));
    }

    @Test
    void testThrowExceptionWhenStudentIsNull() {
        Enrollment enrollment = new Enrollment();
        Course course = Course.builder().id(1L).build();

        assertThatThrownBy(() -> enrollment.createNewEnrollment(null, course))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Please provide a student for enrollment");
    }

    @Test
    void testThrowExceptionWhenCourseIsNull() {
        Enrollment enrollment = new Enrollment();
        Student student = Student.builder().id(1L).build();

        assertThatThrownBy(() -> enrollment.createNewEnrollment(student, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Please provide a course for enrollment");
    }

    @Test
    void testUpdateProgressSuccess() {
        Enrollment enrollment = new Enrollment();
        int newProgress = 50;

        enrollment.updateProgress(newProgress);

        assertThat(enrollment.getProgress()).isEqualTo(newProgress);
    }

    @Test
    void testThrowExceptionForNegativeProgress() {
        Enrollment enrollment = new Enrollment();

        assertThatThrownBy(() -> enrollment.updateProgress(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("be between 0 and 100");
    }

    @Test
    void testThrowExceptionForProgressAbove100() {
        Enrollment enrollment = new Enrollment();

        assertThatThrownBy(() -> enrollment.updateProgress(101))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("be between 0 and 100");
    }

}
