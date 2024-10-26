package com.question_bank_backend.course;


import com.question_bank_backend.semester.SemesterEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Course")
public class CourseEntity
{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;


    @Column(name = "Course_Name")
    private String name;


    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true,mappedBy = "course")
    private List<SemesterEntity> semester;





}
