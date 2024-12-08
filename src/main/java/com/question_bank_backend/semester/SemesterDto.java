package com.question_bank_backend.semester;


import com.question_bank_backend.course.CourseEntity;
import com.question_bank_backend.subject.SubjectEntity;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SemesterDto {

    private int semester;

    private CourseEntity course;

    private List<SubjectEntity> subjects;

}
