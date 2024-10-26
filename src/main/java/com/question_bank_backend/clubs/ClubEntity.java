package com.question_bank_backend.clubs;


import com.question_bank_backend.activity.ActivityEntity;
import com.question_bank_backend.admin.AdminEntity;
import com.question_bank_backend.clubmembers.ClubmemberEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Club")
public class ClubEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "ClubName")
    private String name;


    private String verifiedBy;

    @ManyToOne
    @JoinColumn(name = "Admin_Id", nullable = false)
    private AdminEntity admin;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "club")
    private List<ClubmemberEntity> member;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "club")
    private List<ActivityEntity> activity;


}
