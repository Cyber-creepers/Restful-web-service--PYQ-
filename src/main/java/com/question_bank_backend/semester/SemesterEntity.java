package com.question_bank_backend.semester;

import com.question_bank_backend.course.CourseEntity;
import com.question_bank_backend.subject.SubjectEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Semester")
public class SemesterEntity
{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "Semester_Name")
    private int semester;


    @ManyToOne
    @JoinColumn(name = "Course_Id", nullable = false)
    private CourseEntity course;


    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true,mappedBy = "semester")
    private List<SubjectEntity> subjects;


}
