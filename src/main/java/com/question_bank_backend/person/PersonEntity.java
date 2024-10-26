package com.question_bank_backend.person;


import com.question_bank_backend.otpverification.OtpVerificationEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Person")
@Inheritance(strategy = InheritanceType.JOINED)
public class PersonEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    private Long phone_No;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "Photo",length = 100000000)
    private byte[] photo;

    private String password;

    private String email;

    @OneToOne(mappedBy = "personEntity",cascade = CascadeType.ALL)
    private OtpVerificationEntity otpverification;


}
