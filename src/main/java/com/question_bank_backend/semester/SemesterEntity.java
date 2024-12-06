package com.question_bank_backend.semester;

import com.question_bank_backend.course.CourseEntity;
import com.question_bank_backend.subject.SubjectEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "Semester")
public class SemesterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;


    private int sem;


    @ManyToOne
    @JoinColumn(name = "course_id")
    private CourseEntity course;


    @OneToMany(mappedBy = "semester", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SubjectEntity> subjects;


}
