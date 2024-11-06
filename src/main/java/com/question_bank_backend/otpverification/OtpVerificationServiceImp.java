package com.question_bank_backend.otpverification;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class OtpVerificationServiceImp implements OtpVerificationService
{

    private final OtpVerificationRepository otpVerificationRepository;
    private final ObjectMapper objectMapper;

    OtpVerificationServiceImp(OtpVerificationRepository otpVerificationRepository, ObjectMapper objectMapper){
        this.otpVerificationRepository = otpVerificationRepository;
        this.objectMapper = objectMapper;
    }

}
