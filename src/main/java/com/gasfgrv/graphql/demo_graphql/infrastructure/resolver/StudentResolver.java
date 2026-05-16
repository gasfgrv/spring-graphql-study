package com.gasfgrv.graphql.demo_graphql.infrastructure.resolver;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.gasfgrv.graphql.demo_graphql.domain.model.Enrollment;
import com.gasfgrv.graphql.demo_graphql.domain.model.Student;
import com.gasfgrv.graphql.demo_graphql.domain.port.in.EnrollmentUsecasePort;
import com.gasfgrv.graphql.demo_graphql.domain.port.in.StudentUsecasePort;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class StudentResolver {

    private final EnrollmentUsecasePort enrollmentUsecase;
    private final StudentUsecasePort studentUsecase;

    @QueryMapping
    public List<Student> findAllStudents() {
        return studentUsecase.findAllStudents();
    }

    @QueryMapping
    public Student findStudentById(@Argument Long id) {
        return studentUsecase.findStudentById(id).get();
    }

    @BatchMapping(field = "enrollments", typeName = "Student")
    public Map<Student, List<Enrollment>> enrollments(List<Student> students) {
        List<Long> studentIds = students.stream()
                .map(Student::getId)
                .toList();

        List<Enrollment> allEnrollments = enrollmentUsecase.findAllEnrollmentsByStudentIds(studentIds);

        Map<Long, List<Enrollment>> enrollmentsByStudentId = allEnrollments.stream()
                .collect(Collectors.groupingBy(enrollment -> enrollment.getStudent().getId()));

        return students.stream()
                .collect(Collectors.toMap(
                        student -> student,
                        student -> enrollmentsByStudentId.getOrDefault(student.getId(), List.of())));
    }

    @MutationMapping
    public Student createStudent(@Argument String name, @Argument String email) {
        return studentUsecase.createStudent(name, email);
    }

    @MutationMapping
    public Student updateStudent(@Argument Long id, @Argument String name, @Argument String email) {
        return studentUsecase.updateStudent(id, name, email);
    }

    @MutationMapping
    public boolean deleteStudent(@Argument Long id) {
        return studentUsecase.deleteStudent(id);
    }

}
