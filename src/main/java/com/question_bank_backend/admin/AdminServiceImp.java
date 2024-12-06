package com.question_bank_backend.admin;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.question_bank_backend.otpverification.OtpVerificationEntity;
import com.question_bank_backend.utility.EmailUtil;
import com.question_bank_backend.utility.FileUtil;
import com.question_bank_backend.utility.OtpUtil;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;

@Service("adminUserDetailsService")
public class AdminServiceImp implements AdminService {

    private final AdminRepository adminRepository;

    private final ObjectMapper objectMapper;

    private final OtpUtil otpUtil;

    private final EmailUtil emailUtil;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final FileUtil fileUtil;

    @Value("${project.profile-pic}")
    private String path;

    @Value("${base.url}")
    private String baseUrl;


    public AdminServiceImp(AdminRepository adminRepository, ObjectMapper objectMapper, OtpUtil otpUtil, EmailUtil emailUtil, BCryptPasswordEncoder bCryptPasswordEncoder, FileUtil fileUtil) {
        this.adminRepository = adminRepository;
        this.objectMapper = objectMapper;
        this.otpUtil = otpUtil;
        this.emailUtil = emailUtil;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.fileUtil = fileUtil;
    }

    @Override
    public AdminDto register(AdminDto adminDto, MultipartFile file) throws IOException {
        if (Files.exists(Paths.get(path + File.separator + file.getOriginalFilename()))) {
            throw new FileAlreadyExistsException("File already exists! Please enter another file name!");
        }

        // Check if a Admin with the same email already exists
        if (adminRepository.existsByEmail(adminDto.getEmail())) {
            throw new RuntimeException("User with email " + adminDto.getEmail() + "' already exists");
        }

        StringBuilder otp = otpUtil.generateOtp();

        String otpOutput = otp.toString();

        try {
            emailUtil.sendOtpEmail(adminDto.getEmail(), otpOutput);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send OTP to email " + adminDto.getEmail() + "' please try again");
        }

        String uploadFileName = fileUtil.uploadFile(path, file);

        String posterUrl = baseUrl + "/api/v1/admin/download-pic?fileName=" + uploadFileName;


        AdminEntity adminEntity = new AdminEntity();
        adminEntity.setEmail(adminDto.getEmail());
        adminEntity.setPassword(bCryptPasswordEncoder.encode(adminDto.getPassword()));
        adminEntity.setName(adminDto.getName());
        adminEntity.setVerifiedBy(adminDto.getVerifiedBy());
        adminEntity.setFileName(uploadFileName);
        adminEntity.setPhone_No(adminDto.getPhone_No());

        OtpVerificationEntity otpVerificationEntity = new OtpVerificationEntity();
        otpVerificationEntity.setOtp(otpOutput);
        otpVerificationEntity.setStatus("NotVerified");
        otpVerificationEntity.setSendTime(LocalDateTime.now());

        //set both side of the relationship
        otpVerificationEntity.setPersonEntity(adminEntity);
        adminEntity.setOtpVerification(otpVerificationEntity);

        // Save the studentEntity which should cascade and save the OtpVerificationEntity
        adminRepository.save(adminEntity);

        adminDto.setPhoto(uploadFileName);
        adminDto.setPhotoUrl(posterUrl);

        return adminDto;
    }


    @Override
    public String verifyAccount(String email, String otp) {
        AdminEntity adminEntity = adminRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User withe email '" + email + "' do not exist"));
        if (adminEntity != null
                && adminEntity.getOtpVerification().getOtp().equals(otp)
                && Duration.between(adminEntity.getOtpVerification().getSendTime(), LocalDateTime.now()).getSeconds() <= 60) {

            adminEntity.getOtpVerification().setStatus("Verified");
            adminRepository.save(adminEntity);
            return "Otp verified you can login";
        } else {
            return "Please regenerate Otp and try again";
        }
    }

    @Override
    public String regenerateOtp(String email) {
        AdminEntity adminEntity = adminRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User with this email '" + email + "' do not exist"));
        if (adminEntity != null) {

            StringBuilder otp = otpUtil.generateOtp();
            String otpOutput = otp.toString();

            try {
                emailUtil.sendOtpEmail(email, otpOutput);
            } catch (MessagingException e) {
                throw new RuntimeException("Unable to send Otp to email please try again");
            }

            OtpVerificationEntity otpVerificationEntity = adminEntity.getOtpVerification();
            otpVerificationEntity.setOtp(otpOutput);
            otpVerificationEntity.setSendTime(LocalDateTime.now());
            adminRepository.save(adminEntity);
            return "Email send successfully please verify your Account within one minute";
        } else {
            return "Failed to send Email";
        }
    }

    @Override
    public String forgetPassword(String email) {
        AdminEntity adminEntity = adminRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User with this email '" + email + "' do not exist"));

        if (adminEntity != null) {

            StringBuilder otp = otpUtil.generateOtp();
            String otpOutput = otp.toString();

            OtpVerificationEntity otpVerificationEntity = adminEntity.getOtpVerification();
            otpVerificationEntity.setOtp(otpOutput);
            otpVerificationEntity.setSendTime(LocalDateTime.now());
            adminRepository.save(adminEntity);

            try {
                emailUtil.setPasswordEmail(email, otpOutput);
            } catch (MessagingException e) {
                throw new RuntimeException("Unable to send email please try again");
            }

            return "Email send Successfully visit your mail for Further assistant";
        }

        return "Failed to send Email For Forgot Password";
    }

    @Override
    public String setPassword(String email, String password) {
        AdminEntity adminEntity = adminRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User with this email '" + email + "' do not exist"));

        if (adminEntity != null) {
            adminEntity.setPassword(bCryptPasswordEncoder.encode(password));
            adminRepository.save(adminEntity);
            return "Password set successfully";
        } else {
            return "Failed to set Password";
        }
    }

    @Override
    public String changePassword(String otp, String newPassword, String email) {

        AdminEntity adminEntity = adminRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("User not found with this email '"+email+"' "));

        if (adminEntity != null
         && Duration.between(adminEntity.getOtpVerification().getSendTime(), LocalDateTime.now()).getSeconds() < (60)) {


            OtpVerificationEntity otpVerificationEntity = adminEntity.getOtpVerification();
            if (!otpVerificationEntity.getOtp().equals(otp)){
                throw new RuntimeException("Otp doesn't match");
            }
            otpVerificationEntity.setStatus("Verified");
            adminEntity.setPassword(bCryptPasswordEncoder.encode(newPassword));
            adminRepository.save(adminEntity);
            return "Password Change Successfully Email : "+ email +"' now you can login";

        }else{
            throw new RuntimeException("Please regenerate Otp and try again");
        }

    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AdminEntity adminEntity = adminRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User with this email '" + email + "' do not exist"));

        return new AdminPrincipal(adminEntity);
    }
}
