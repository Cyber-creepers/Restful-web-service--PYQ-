package com.question_bank_backend.admin;

import com.question_bank_backend.clubs.ClubEntity;
import com.question_bank_backend.course.CourseEntity;
import com.question_bank_backend.person.PersonEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Admin")
@PrimaryKeyJoinColumn(name = "ID")
public class AdminEntity extends PersonEntity {


    @Column(name = "VerifiedBy", nullable = false)
    private String verifiedBy;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "admin", orphanRemoval = true)
    private List<ClubEntity> clubEntity;


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "Admin_Ref")
    private List<CourseEntity> course;



}
