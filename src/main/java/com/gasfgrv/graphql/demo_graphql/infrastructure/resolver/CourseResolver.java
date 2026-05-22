package com.gasfgrv.graphql.demo_graphql.infrastructure.resolver;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.gasfgrv.graphql.demo_graphql.domain.model.Course;
import com.gasfgrv.graphql.demo_graphql.domain.model.Enrollment;
import com.gasfgrv.graphql.demo_graphql.domain.port.in.CourseUsecasePort;
import com.gasfgrv.graphql.demo_graphql.domain.port.in.EnrollmentUsecasePort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CourseResolver {

    private final EnrollmentUsecasePort enrollmentUsecase;
    private final CourseUsecasePort courseUsecase;

    @QueryMapping
    public List<Course> findAllCourses() {
        log.info("GraphQL Query: findAllCourses");
        return courseUsecase.findAllCourses();
    }

    @QueryMapping
    public Course findCourseById(@Argument Long id) {
        log.info("GraphQL Query: findCourseById with ID: {}", id);
        return courseUsecase.findCourseById(id).get();
    }

    @BatchMapping(field = "enrollments", typeName = "Course")
    public Map<Course, List<Enrollment>> enrollments(List<Course> courses) {
        log.info("GraphQL BatchMapping: enrollments for {} courses", courses.size());
        List<Long> courseIds = courses.stream()
                .map(Course::getId)
                .toList();

        List<Enrollment> allEnrollments = enrollmentUsecase.findAllEnrollmentsByCourseIds(courseIds);

        Map<Long, List<Enrollment>> enrollmentsByCourseId = allEnrollments.stream()
                .collect(Collectors.groupingBy(enrollment -> enrollment.getCourse().getId()));

        return courses.stream()
                .collect(Collectors.toMap(
                        course -> course,
                        course -> enrollmentsByCourseId.getOrDefault(course.getId(), List.of())));
    }

    @MutationMapping
    public Course createCourse(@Argument String title, @Argument String description, @Argument String level) {
        log.info("GraphQL Mutation: createCourse with title: {}", title);
        return courseUsecase.createCourse(title, description, level);
    }

    @MutationMapping
    public Course updateCourse(@Argument Long id, @Argument String title, @Argument String description,
            @Argument String level) {
        log.info("GraphQL Mutation: updateCourse with ID: {}", id);
        return courseUsecase.updateCourse(id, title, description, level);
    }

    @MutationMapping
    public boolean deleteCourse(@Argument Long id) {
        log.info("GraphQL Mutation: deleteCourse with ID: {}", id);
        return courseUsecase.deleteCourse(id);
    }

}
