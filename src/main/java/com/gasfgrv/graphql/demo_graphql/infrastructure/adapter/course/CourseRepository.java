package com.gasfgrv.graphql.demo_graphql.infrastructure.adapter.course;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.gasfgrv.graphql.demo_graphql.domain.model.Course;
import com.gasfgrv.graphql.demo_graphql.domain.port.out.CourseRepositoryPort;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CourseRepository implements CourseRepositoryPort {

    private final CourseMapper mapper;
    private final CourseJpaRepository jpaRepository;

    @Override
    public List<Course> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Course> findById(long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Course> findAllByIds(List<Long> ids) {
        return jpaRepository.findAllById(ids)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Course save(Course course) {
        CourseEntity save = jpaRepository.save(mapper.toEntity(course));
        return mapper.toDomain(save);
    }

    @Override
    public boolean deleteById(long id) {
        Optional<CourseEntity> course = jpaRepository.findById(id);

        if (course.isEmpty()) {
            return false;
        }

        CourseEntity courseToBeDeleted = course.get();
        jpaRepository.deleteById(courseToBeDeleted.getId());
        return true;
    }
}
