package com.question_bank_backend.student;

public interface StudentService
{
    StudentDto register(StudentDto studentDto);

    StudentDto login(String email, String password);

    String verifyAccount(String email, String otp);

    String regenerateOtp(String email);

    String forgetPassword(String email);

    String setPassword(String email, String password);


}
