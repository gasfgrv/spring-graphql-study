package com.gasfgrv.graphql.demo_graphql.infrastructure.adapter.course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface CourseJpaRepository extends JpaRepository<CourseEntity, Long> {

}
