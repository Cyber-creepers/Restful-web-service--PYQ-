package com.question_bank_backend.student;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StudentService extends UserDetailsService
{
    StudentDto register(StudentDto studentDto, MultipartFile file) throws IOException;

    String verifyAccount(String email, String otp);

    String regenerateOtp(String email);

    String forgetPassword(String email);

    String setPassword(String email, String password);

    String changePassword(String otp, String newPassword, String email);
}
