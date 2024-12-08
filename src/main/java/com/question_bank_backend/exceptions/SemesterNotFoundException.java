package com.question_bank_backend.exceptions;

public class SemesterNotFoundException extends RuntimeException {

    public SemesterNotFoundException(String message) {
        super(message);
    }
}
