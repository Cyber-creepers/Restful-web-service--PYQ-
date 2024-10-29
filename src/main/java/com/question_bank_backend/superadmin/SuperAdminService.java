package com.question_bank_backend.superadmin;


import org.springframework.security.core.userdetails.UserDetailsService;

public interface SuperAdminService extends UserDetailsService {


    SuperAdminDto register(SuperAdminDto superAdminDto);

    String verifyAccount(String email, String otp);

    String regenerateOtp(String email);

    SuperAdminDto login(String email, String password);

    String forgetPassword(String email);

    String setPassword(String email, String newPassword);

    String changePassword(String otp, String newPassword,String email);

}
