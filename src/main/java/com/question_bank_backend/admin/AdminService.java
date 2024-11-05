package com.question_bank_backend.admin;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AdminService {

    AdminDto register(AdminDto adminDto, MultipartFile file) throws IOException;

    String verifyAccount(String email, String otp);

    String regenerateOtp(String email);

    String forgetPassword(String email);

    String setPassword(String email, String password);


}
