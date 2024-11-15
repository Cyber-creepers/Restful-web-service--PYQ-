package com.question_bank_backend.question;


import com.question_bank_backend.subject.SubjectEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "Question")
public class QuestionEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;


    @Column(name = "Question_Year")
    private int year;

    private String question;


    @ManyToOne
    @JoinColumn(name = "subject_id")
    private SubjectEntity subject;


}
