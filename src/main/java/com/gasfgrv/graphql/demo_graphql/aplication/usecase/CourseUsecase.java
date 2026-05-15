package com.gasfgrv.graphql.demo_graphql.aplication.usecase;

import com.gasfgrv.graphql.demo_graphql.aplication.exceptions.CourseNotFoundException;
import com.gasfgrv.graphql.demo_graphql.domain.model.Course;
import com.gasfgrv.graphql.demo_graphql.domain.port.in.CourseUsecasePort;
import com.gasfgrv.graphql.demo_graphql.domain.port.out.CourseRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CourseUsecase implements CourseUsecasePort {

    private final CourseRepositoryPort repository;

    @Override
    public List<Course> findAllCourses() {
        return repository.findAll();
    }

    @Override
    public Optional<Course> findCourseById(long id) {
        return Optional.of(repository.findById(id)
                .orElseThrow(CourseNotFoundException::new));
    }

    @Override
    public List<Course> findAllCoursesByIds(List<Long> ids) {
        return repository.findAllByIds(ids);
    }

    @Override
    public Course createCourse(String title, String description, String level) {
        Course course = new Course()
                .validateTitle(title)
                .validateDescription(description)
                .validateLevel(level)
                .createCourse();
        return repository.save(course);
    }

    @Override
    public Course updateCourse(long id, String title, String description, String level) {
        Course updated = repository.findById(id)
                .map(course -> course.updateData(title, description, level))
                .orElseThrow(CourseNotFoundException::new);
        return repository.save(updated);
    }

    @Override
    public boolean deleteCourse(long id) {
        return repository.deleteById(id);
    }

}
