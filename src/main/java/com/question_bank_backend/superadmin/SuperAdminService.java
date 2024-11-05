package com.question_bank_backend.superadmin;


import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface SuperAdminService extends UserDetailsService {


    SuperAdminDto register(SuperAdminDto superAdminDto, MultipartFile file) throws IOException;

    String verifyAccount(String email, String otp);

    String regenerateOtp(String email);

    String forgetPassword(String email);

    String setPassword(String email, String newPassword);

    String changePassword(String otp, String newPassword, String email);

}
