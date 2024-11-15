package com.question_bank_backend.course;


import com.question_bank_backend.semester.SemesterEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto
{

    private String name;

    private String courseId;

    private List<SemesterEntity> semester;



}
