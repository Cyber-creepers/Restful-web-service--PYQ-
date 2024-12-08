package com.question_bank_backend.exceptions;

public class CourseNotFoundException extends RuntimeException {

    public CourseNotFoundException(String message){
        super(message);
    }


}
