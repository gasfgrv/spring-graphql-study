package com.gasfgrv.graphql.demo_graphql.aplication.exceptions;

public class StudentNotFoundException extends RuntimeException {

    public StudentNotFoundException() {
        super("Student not found");
    }

}
