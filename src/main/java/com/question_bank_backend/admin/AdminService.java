package com.question_bank_backend.admin;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AdminService  extends UserDetailsService {

    AdminDto register(AdminDto adminDto, MultipartFile file) throws IOException;

    String verifyAccount(String email, String otp);

    String regenerateOtp(String email);

    String forgetPassword(String email);

    String setPassword(String email, String password);

    String changePassword(String otp, String newPassword, String email);



}
