package com.question_bank_backend.subject;


import com.question_bank_backend.question.QuestionEntity;
import com.question_bank_backend.semester.SemesterEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "Subject")
public class SubjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;



    private String subjectName;

    private String subjectCode;


    @ManyToOne
    @JoinColumn(name = "semester_id")
    private SemesterEntity semester;



    @OneToMany(mappedBy = "subject",cascade =  CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<QuestionEntity> questions;

    public SubjectEntity(String subjectName, String subjectCode) {
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
    }

    public SubjectEntity() {

    }
}
