package com.gasfgrv.graphql.demo_graphql.infrastructure.adapter.enrollment;

import java.time.LocalDateTime;

import com.gasfgrv.graphql.demo_graphql.infrastructure.adapter.course.CourseEntity;
import com.gasfgrv.graphql.demo_graphql.infrastructure.adapter.student.StudentEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "enrollments", uniqueConstraints = {
        @UniqueConstraint(name = "unique_student_course", columnNames = { "student_id", "course_id" }) })
public class EnrollmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private StudentEntity student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private CourseEntity course;

    @Column(nullable = false)
    private int progress;

    @Column(name = "enrolled_at", nullable = false, updatable = false)
    private LocalDateTime enrolledAt;

}
