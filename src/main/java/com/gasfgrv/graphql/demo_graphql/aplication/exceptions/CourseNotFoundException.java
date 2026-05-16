package com.gasfgrv.graphql.demo_graphql.aplication.exceptions;

public class CourseNotFoundException extends RuntimeException {

    public CourseNotFoundException() {
        super("Course not found");
    }

    public CourseNotFoundException(String message) {
        super(message);
    }

}
