package com.gasfgrv.graphql.demo_graphql.aplication.usecase;

import com.gasfgrv.graphql.demo_graphql.aplication.exceptions.CourseNotFoundException;
import com.gasfgrv.graphql.demo_graphql.domain.model.Course;
import com.gasfgrv.graphql.demo_graphql.domain.port.in.CourseUsecasePort;
import com.gasfgrv.graphql.demo_graphql.domain.port.out.CourseRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class CourseUsecase implements CourseUsecasePort {

    private final CourseRepositoryPort repository;

    @Override
    public List<Course> findAllCourses() {
        log.info("Usecase: finding all courses");
        return repository.findAll();
    }

    @Override
    public Optional<Course> findCourseById(long id) {
        log.info("Usecase: finding course by ID: {}", id);
        return Optional.of(repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Course with ID {} not found", id);
                    return new CourseNotFoundException();
                }));
    }

    @Override
    public List<Course> findAllCoursesByIds(List<Long> ids) {
        log.info("Usecase: finding courses by IDs: {}", ids);
        return repository.findAllByIds(ids);
    }

    @Override
    public Course createCourse(String title, String description, String level) {
        log.info("Usecase: creating course with title: {}", title);
        Course course = new Course()
                .validateTitle(title)
                .validateDescription(description)
                .validateLevel(level)
                .createCourse();
        return repository.save(course);
    }

    @Override
    public Course updateCourse(long id, String title, String description, String level) {
        log.info("Usecase: updating course with ID: {}", id);
        Course updated = repository.findById(id)
                .map(course -> course.updateData(title, description, level))
                .orElseThrow(() -> {
                    log.warn("Course with ID {} not found for update", id);
                    return new CourseNotFoundException();
                });
        return repository.save(updated);
    }

    @Override
    public boolean deleteCourse(long id) {
        log.info("Usecase: deleting course with ID: {}", id);
        return repository.deleteById(id);
    }

}
