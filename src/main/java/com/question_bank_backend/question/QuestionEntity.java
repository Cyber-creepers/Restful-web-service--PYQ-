package com.question_bank_backend.question;


import com.question_bank_backend.subject.SubjectEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Question")
public class QuestionEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;


    @Column(name = "Question_Year")
    private int year;

    @Column(name = "Uploaded_By")
    private String uploadedBy;


    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "Question", length = 1000000000)
    private byte[] question;


    @ManyToOne
    @JoinColumn(name = "Subject_Id", nullable = false)
    private SubjectEntity subject;


}
