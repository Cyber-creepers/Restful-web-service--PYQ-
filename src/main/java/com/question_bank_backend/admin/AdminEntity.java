package com.question_bank_backend.admin;

import com.question_bank_backend.clubs.ClubEntity;
import com.question_bank_backend.course.CourseEntity;
import com.question_bank_backend.person.PersonEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "Admin")
@PrimaryKeyJoinColumn(name = "ID")
public class AdminEntity extends PersonEntity {


    @Column(name = "VerifiedBy", nullable = false)
    private String verifiedBy;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "admin", orphanRemoval = true)
    private List<ClubEntity> clubEntity;


}
