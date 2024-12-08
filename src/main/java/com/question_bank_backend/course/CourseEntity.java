package com.question_bank_backend.course;


import com.question_bank_backend.semester.SemesterEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "Course")
public class CourseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String courseId;

    private String courseFullName;

    private String courseShortName;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SemesterEntity> semester;

    public CourseEntity(String courseFullName, String courseShortName){
        this.courseFullName = courseFullName;
        this.courseShortName = courseShortName;
    }

    public CourseEntity(){

    }


}
