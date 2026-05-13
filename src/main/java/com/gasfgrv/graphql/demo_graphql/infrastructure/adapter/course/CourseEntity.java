package com.gasfgrv.graphql.demo_graphql.infrastructure.adapter.course;

import java.util.ArrayList;
import java.util.List;

import com.gasfgrv.graphql.demo_graphql.domain.model.Level;
import com.gasfgrv.graphql.demo_graphql.infrastructure.adapter.enrollment.EnrollmentEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "courses")
@Getter
@NoArgsConstructor
public class CourseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private Level level;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<EnrollmentEntity> enrollments = new ArrayList<>();

}
