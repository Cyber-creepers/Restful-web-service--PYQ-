package com.question_bank_backend.subject;


import com.question_bank_backend.semester.SemesterEntity;
import lombok.*;


@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubjectDto {


    private String subjectName;

    private String subjectCode;

    private SemesterEntity semester;


}
