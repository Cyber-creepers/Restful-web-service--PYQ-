package com.question_bank_backend.question;


import com.question_bank_backend.subject.SubjectEntity;
import lombok.Data;

@Data
public class AddQuestionRequest {

    private int year;

    private String question;

    private SubjectEntity subject;


}
