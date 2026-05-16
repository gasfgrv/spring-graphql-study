package com.gasfgrv.graphql.demo_graphql.aplication.exceptions;

public class EnrollmentNotFoundException extends RuntimeException {

    public EnrollmentNotFoundException() {
        super("Enrollment not found");
    }

    public EnrollmentNotFoundException(String message) {
        super(message);
    }

}
