package com.question_bank_backend.activity;


import com.question_bank_backend.clubs.ClubEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Activity")
public class ActivityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;


    @Column(name = "ActiviTyStatus")
    private String status;

    @Column(name = "ActivityName")
    private byte[] post;


    @ManyToOne
    @JoinColumn(name = "Club_Id", nullable = false)
    private ClubEntity club;


}
