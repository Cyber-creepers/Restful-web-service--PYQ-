package com.question_bank_backend.student;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface StudentService extends UserDetailsService
{
    StudentDto register(StudentDto studentDto);

    StudentDto login(String email, String password);

    String verifyAccount(String email, String otp);

    String regenerateOtp(String email);

    String forgetPassword(String email);

    String setPassword(String email, String password);


}
