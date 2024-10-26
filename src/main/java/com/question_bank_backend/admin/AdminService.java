package com.question_bank_backend.admin;

public interface AdminService {
    AdminDto register(AdminDto adminDto);

    AdminDto login(String email, String password);

    String verifyAccount(String email, String otp);

    String regenerateOtp(String email);

    String forgetPassword(String email);

    String setPassword(String email, String password);


}
