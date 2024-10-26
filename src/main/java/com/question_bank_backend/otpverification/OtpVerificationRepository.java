package com.question_bank_backend.otpverification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OtpVerificationRepository extends JpaRepository<OtpVerificationEntity,String>
{


}
