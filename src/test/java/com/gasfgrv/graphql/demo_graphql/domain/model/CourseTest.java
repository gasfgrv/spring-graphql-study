package com.gasfgrv.graphql.demo_graphql.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class CourseTest {

    @Test
    void testValidateTitleSuccess() {
        Course course = new Course();
        String validTitle = "Spring GraphQL Expert";

        course.validateTitle(validTitle);

        assertThat(course.getTitle()).isEqualTo(validTitle);
    }

    @Test
    void testThrowExceptionWhenTitleIsNull() {
        Course course = new Course();

        assertThatThrownBy(() -> course.validateTitle(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("title id mandatory");
    }

    @Test
    void testThrowExceptionWhenTitleIsBlank() {
        Course course = new Course();

        assertThatThrownBy(() -> course.validateTitle(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("title is blank");
    }

    @Test
    void testThrowExceptionWhenTitleIsTooLong() {
        Course course = new Course();
        String longTitle = "a".repeat(201);

        assertThatThrownBy(() -> course.validateTitle(longTitle))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Title length is greater than 200");
    }

    @Test
    void testValidateDescriptionSuccess() {
        Course course = new Course();
        String description = "Um curso completo sobre Spring e GraphQL";

        course.validateDescription(description);

        assertThat(course.getDescription()).isEqualTo(description);
    }

    @Test
    void testThrowExceptionWhenDescriptionIsTooLong() {
        Course course = new Course();
        String longDescription = "a".repeat(201);

        assertThatThrownBy(() -> course.validateDescription(longDescription))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Description length is greater than 200");
    }

    @Test
    void testValidateLevelSuccess() {
        Course course = new Course();
        String level = "Avançado";

        course.validateLevel(level);

        assertThat(course.getLevel()).isEqualTo(Level.ADVANCED);
    }

    @Test
    void testThrowExceptionForInvalidLevel() {
        Course course = new Course();

        assertThatThrownBy(() -> course.validateLevel("Master"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid level");
    }

    @Test
    void testInitializeEnrollmentsOnCreate() {
        Course course = new Course();

        course.createCourse();

        assertThat(course.getEnrollments()).isNotNull().isEmpty();
    }

    @Test
    void testUpdateDataSuccess() {
        Course course = new Course();
        String title = "Novo Título";
        String description = "Nova Descrição";
        String level = "Iniciante";

        course.updateData(title, description, level);

        assertThat(course.getTitle()).isEqualTo(title);
        assertThat(course.getDescription()).isEqualTo(description);
        assertThat(course.getLevel()).isEqualTo(Level.INITIAL);
    }

}
