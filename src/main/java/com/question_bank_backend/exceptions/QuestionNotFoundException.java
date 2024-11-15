package com.question_bank_backend.exceptions;



public class QuestionNotFoundException extends RuntimeException
{

    public QuestionNotFoundException(String message) {
        super(message);
    }

}
