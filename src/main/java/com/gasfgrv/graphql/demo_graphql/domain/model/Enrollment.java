package com.gasfgrv.graphql.demo_graphql.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class Enrollment {
    private long id;
    private Student student;
    private Course course;
    private int progress;
    private LocalDateTime enrolledAt;

    public Enrollment createNewEnrollment(Student student, Course course) {
        this.student = Objects.requireNonNull(student, "Please provide a student for enrollment");
        this.course = Objects.requireNonNull(course, "Please provide a course for enrollment");
        this.progress = 0;
        this.enrolledAt = LocalDateTime.now();
        return this;
    }

    public Enrollment updateProgress(int progress) {
        if (progress < 0 || progress > 100) {
            throw new IllegalArgumentException("The value of progress should be between 0 and 100");
        }

        this.progress = progress;
        return this;
    }

}
