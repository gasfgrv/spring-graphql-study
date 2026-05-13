package com.gasfgrv.graphql.demo_graphql.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Student {

    private Long id;
    private String name;
    private String email;
    private LocalDateTime createdAt;
    private List<Enrollment> enrollment;

    public Student validateName(String name) {
        Objects.requireNonNull(name, "name is mandatory");

        if (name.isBlank()) {
            throw new IllegalArgumentException("name is blank");
        }

        if (name.length() > 150) {
            throw new IllegalArgumentException("name length is greater than 150");
        }

        this.name = name;
        return this;
    }

    public Student validateEmail(String email) {
        Objects.requireNonNull("email is mandatory");

        if (!email.contains("@")) {
            throw new IllegalArgumentException("Email inválido");
        }

        if (email.length() > 150) {
            throw new IllegalArgumentException("email length is greater than 150");
        }

        if (name.isBlank()) {
            throw new IllegalArgumentException("email is blank");
        }

        this.email = email;
        return this;
    }

    public Student createStudent() {
        this.createdAt = LocalDateTime.now();
        this.enrollment = new ArrayList<>();
        return this;
    }

    public Student updateData(String name, String email) {
        return this.validateName(name)
                .validateEmail(email);
    }

}
