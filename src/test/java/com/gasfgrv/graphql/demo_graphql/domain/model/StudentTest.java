package com.gasfgrv.graphql.demo_graphql.domain.model;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class StudentTest {

    @Test
    void testValidateNameSuccess() {
        Student student = new Student();
        String validName = "Gustavo";

        student.validateName(validName);

        assertThat(student.getName()).isEqualTo(validName);
    }

    @Test
    void testThrowExceptionWhenNameIsNull() {
        Student student = new Student();

        assertThatThrownBy(() -> student.validateName(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("name is mandatory");
    }

    @Test
    void testThrowExceptionWhenNameIsBlank() {
        Student student = new Student();

        assertThatThrownBy(() -> student.validateName("  "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("name is blank");
    }

    @Test
    void testThrowExceptionWhenNameIsTooLong() {
        Student student = new Student();
        String longName = "a".repeat(151);

        assertThatThrownBy(() -> student.validateName(longName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("name length is greater than 150");
    }

    @Test
    void testValidateEmailSuccess() {
        Student student = new Student();
        String email = "teste@exemplo.com";

        student.validateEmail(email);

        assertThat(student.getEmail()).isEqualTo(email);
    }

    @Test
    void testThrowExceptionWhenEmailIsInvalid() {
        Student student = new Student();

        assertThatThrownBy(() -> student.validateEmail("email-sem-arroba"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email inválido");
    }

    @Test
    void testThrowExceptionWhenEmailIsBlank() {
        Student student = new Student();

        assertThatThrownBy(() -> student.validateEmail(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email inválido");
    }

    @Test
    void testThrowExceptionWhenEmailIsTooLong() {
        Student student = new Student();
        String longEmail = "a".repeat(141) + "@teste.com";

        assertThatThrownBy(() -> student.validateEmail(longEmail))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("email length is greater than 150");
    }

    @Test
    void testInitializeOnCreate() {
        Student student = new Student();

        student.createStudent();

        assertThat(student.getCreatedAt()).isCloseTo(LocalDateTime.now(), within(1, SECONDS));
        assertThat(student.getEnrollments()).isNotNull().isEmpty();
    }

    @Test
    void testUpdateDataSuccess() {
        Student student = new Student();
        String name = "Novo Nome";
        String email = "novo@email.com";

        student.updateData(name, email);

        assertThat(student.getName()).isEqualTo(name);
        assertThat(student.getEmail()).isEqualTo(email);
    }
}
