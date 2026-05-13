package com.gasfgrv.graphql.demo_graphql.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public final class Course {

    private Long id;
    private String title;
    private String text;
    private Level level;
    private List<Enrollment> enrollments;

    public Course validateTitle(String title) {
        Objects.requireNonNull(title, "title id mandatory");

        if (title.isBlank()) {
            throw new IllegalArgumentException("title is blank");
        }

        if (title.length() > 200) {
            throw new IllegalArgumentException("Title length is greater than 200");
        }

        this.title = title;
        return this;
    }

    public Course validateDescription(String description) {
        if (description.length() > 200) {
            throw new IllegalArgumentException("Description length is greater than 200");
        }

        this.text = description;
        return this;
    }

    public Course validateLevel(String level) {
        this.level = Level.getByValueOf(level);
        return this;
    }

    public Course createCourse() {
        this.enrollments = new ArrayList<>();
        return this;
    }

    public Course updateData(String title, String description, String level) {
        return this.validateTitle(title)
                .validateDescription(description)
                .validateLevel(level);
    }

}
