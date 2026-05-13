package com.gasfgrv.graphql.demo_graphql.infrastructure.adapter.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface StudentJpaRepository extends JpaRepository<StudentEntity, Long>{

}
