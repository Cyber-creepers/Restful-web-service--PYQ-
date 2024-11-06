package com.question_bank_backend.otpverification;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "")
public class OtpVerificationController
{

    private final OtpVerificationService otpVerificationService;

    OtpVerificationController(OtpVerificationService otpVerificationService)
    {
        this.otpVerificationService = otpVerificationService;
    }



}
