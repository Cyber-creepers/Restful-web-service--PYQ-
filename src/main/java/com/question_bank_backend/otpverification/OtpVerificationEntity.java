package com.question_bank_backend.otpverification;


import com.question_bank_backend.person.PersonEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OtpVerification")
public class OtpVerificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "OTP", nullable = false)
    private String otp;

    @Column(name = "Status", nullable = false)
    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Send_Time", nullable = false)
    private LocalDateTime sendTime;

    @OneToOne
    @JoinColumn(name = "PersonId", referencedColumnName = "id", nullable = false, unique = true)
    private PersonEntity personEntity;


}
