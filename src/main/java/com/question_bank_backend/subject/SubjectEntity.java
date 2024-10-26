package com.question_bank_backend.subject;


import com.question_bank_backend.question.QuestionEntity;
import com.question_bank_backend.semester.SemesterEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Subject")
public class SubjectEntity
{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;


    @Column(name = "Subject_Name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "Semester_Id",nullable = false)
    private SemesterEntity semester;


    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true,mappedBy = "subject")
    private List<QuestionEntity> questions;

}
