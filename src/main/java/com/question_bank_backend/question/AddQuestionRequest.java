package com.question_bank_backend.question;


import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddQuestionRequest {

    private int year;

    private String question;

    private String subjectId;


}
