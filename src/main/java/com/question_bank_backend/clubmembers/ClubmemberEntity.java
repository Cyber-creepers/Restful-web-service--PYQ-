package com.question_bank_backend.clubmembers;


import com.question_bank_backend.clubs.ClubEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ClubMember")
public class ClubmemberEntity
{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;


    @Column(name = "MemberName",nullable = false)
    private String name;

    @Column(name = "MemberPhNo")
    private int ph_No;

    @Column(name = "MemberPosition")
    private String position;


    @ManyToOne
    @JoinColumn(name = "Club_Id",nullable = false)
    private ClubEntity club;

}
