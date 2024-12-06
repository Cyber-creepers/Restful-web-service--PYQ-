package com.question_bank_backend.superadmin;


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

@Service("superAdminUserDetailsService")
public class SuperAdminServiceImp implements SuperAdminService {

    SuperAdminRepository superAdminRepository;

    BCryptPasswordEncoder bCryptPasswordEncoder;

    ObjectMapper objectMapper;

    OtpUtil otpUtil;

    EmailUtil emailUtil;

    @Value("${project.profile-pic}")
    private String path;

    @Value("${base.url}")
    private String baseUrl;

    private final FileUtil fileUtil;


    public SuperAdminServiceImp(SuperAdminRepository superAdminRepository, BCryptPasswordEncoder bCryptPasswordEncoder, ObjectMapper objectMapper, OtpUtil otpUtil, EmailUtil emailUtil, FileUtil fileUtil) {
        this.superAdminRepository = superAdminRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.objectMapper = objectMapper;
        this.otpUtil = otpUtil;
        this.emailUtil = emailUtil;
        this.fileUtil = fileUtil;
    }

    @Override
    public SuperAdminDto register(SuperAdminDto superAdminDto, MultipartFile file) throws IOException {

        if (Files.exists(Paths.get(path + File.separator + file.getOriginalFilename()))) {
            throw new FileAlreadyExistsException("File already exists! Please enter another file name!");
        }

        // Check if a SuperAdmin with the same email already exists
        if (superAdminRepository.existsByEmail(superAdminDto.getEmail())) {
            throw new RuntimeException("User with email " + superAdminDto.getEmail() + " already exists");
        }


        StringBuilder otp = otpUtil.generateOtp();

        String otpOutput = otp.toString();

        try {
            emailUtil.sendOtpEmail(superAdminDto.getEmail(), otpOutput);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send otp to email please try again");
        }

        String uploadedFileName = fileUtil.uploadFile(path, file);


        String posterUrl = baseUrl + "/api/v1/superAdmin/download-pic?fileName=" + uploadedFileName;

        SuperAdminEntity superAdminEntity = new SuperAdminEntity();
        superAdminEntity.setEmail(superAdminDto.getEmail());
        superAdminEntity.setPassword(bCryptPasswordEncoder.encode(superAdminDto.getPassword()));
        superAdminEntity.setName(superAdminDto.getName());
        superAdminEntity.setFileName(uploadedFileName);
        superAdminEntity.setPhone_No(superAdminDto.getPhone_No());

        OtpVerificationEntity otpVerificationEntity = new OtpVerificationEntity();
        otpVerificationEntity.setOtp(otpOutput);
        otpVerificationEntity.setSendTime(LocalDateTime.now());
        otpVerificationEntity.setStatus("NotVerified");

        // Set both sides of the relationship
        otpVerificationEntity.setPersonEntity(superAdminEntity);
        superAdminEntity.setOtpVerification(otpVerificationEntity);

        // Save the SuperAdminEntity which should cascade and save the OtpVerificationEntity
        superAdminRepository.save(superAdminEntity);

        superAdminDto.setPhotoUrl(posterUrl);
        superAdminDto.setPhoto(uploadedFileName);

        return superAdminDto;
    }


    @Override
    public String verifyAccount(String email, String otp) {
        SuperAdminEntity superAdminEntity = superAdminRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found with this email " + email));
        if (superAdminEntity != null
                && superAdminEntity.getOtpVerification().getOtp().equals(otp)
                && Duration.between(superAdminEntity.getOtpVerification().getSendTime(), LocalDateTime.now()).getSeconds() < (60)) {


            superAdminEntity.getOtpVerification().setStatus("Verified");
            superAdminRepository.save(superAdminEntity);
            return "Otp verified you can login";

        } else {
            return "Please regenerate Otp and try again";
        }
    }

    @Override
    public String regenerateOtp(String email) {
        SuperAdminEntity superAdminEntity = superAdminRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found with this email " + email));
        if (superAdminEntity != null) {

            StringBuilder otp = otpUtil.generateOtp();
            String otpOutput = otp.toString();

            try {
                emailUtil.sendOtpEmail(email, otpOutput);
            } catch (MessagingException e) {
                throw new RuntimeException("Unable to send otp to email please try again");
            }

            OtpVerificationEntity otpVerificationEntity = superAdminEntity.getOtpVerification();
            otpVerificationEntity.setOtp(otpOutput);
            otpVerificationEntity.setSendTime(LocalDateTime.now());
            superAdminRepository.save(superAdminEntity);
            return "Email send Successfully please verify Account within One minute ";

        } else {
            return "Failed to send Email ";
        }
    }


    @Override
    public String forgetPassword(String email) {

        SuperAdminEntity superAdminEntity = superAdminRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found with this email " + email));
        if (superAdminEntity != null) {

            StringBuilder otp = otpUtil.generateOtp();
            String otpOutput = otp.toString();

            OtpVerificationEntity otpVerificationEntity = superAdminEntity.getOtpVerification();
            otpVerificationEntity.setOtp(otpOutput);
            otpVerificationEntity.setSendTime(LocalDateTime.now());
            superAdminRepository.save(superAdminEntity);

            try {
                emailUtil.setPasswordEmail(email, otpOutput);
            } catch (MessagingException e) {
                throw new RuntimeException("Unable to send email please try again");
            }

            return "Email send Successfully visit your mail for Further assistant ";

        }
        return "Failed to send Email For Forgot password ";

    }

    @Override
    public String setPassword(String email, String password) {
        SuperAdminEntity superAdminEntity = superAdminRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found with this email " + email));
        if (superAdminEntity != null) {
            superAdminEntity.setPassword(bCryptPasswordEncoder.encode(password));
            superAdminRepository.save(superAdminEntity);
            return "Password set successfully";
        } else {
            return "Failed to set password";
        }
    }

    @Override
    public String changePassword(String otp, String newPassword, String email) {
        SuperAdminEntity superAdminEntity = superAdminRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found with this email '" + email + "' "));
        if (superAdminEntity != null && Duration.between(superAdminEntity.getOtpVerification().getSendTime(), LocalDateTime.now()).getSeconds() < (60)) {


            OtpVerificationEntity otpVerificationEntity = superAdminEntity.getOtpVerification();
            if (!otpVerificationEntity.getOtp().equals(otp)) {
                throw new RuntimeException("Otp doesn't match");
            }
            otpVerificationEntity.setStatus("Verified");
            superAdminEntity.setPassword(bCryptPasswordEncoder.encode(newPassword));
            superAdminRepository.save(superAdminEntity);
            return "Password Change Successfully Email ' : " + email + "' now you can login";
        } else {
            throw new RuntimeException("Please regenerate Otp and try again");
        }

    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        SuperAdminEntity superAdminEntity = superAdminRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found with this email '" + email + "' "));

        return new SuperAdminPrincipal(superAdminEntity);
    }


}
