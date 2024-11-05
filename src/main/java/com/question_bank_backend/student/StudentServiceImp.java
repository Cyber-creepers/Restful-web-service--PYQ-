package com.question_bank_backend.student;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.question_bank_backend.otpverification.OtpVerificationEntity;
import com.question_bank_backend.utility.EmailUtil;
import com.question_bank_backend.utility.FileUtil;
import com.question_bank_backend.utility.OtpUtil;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;

@Service("studentUserDetailsService")
public class StudentServiceImp implements StudentService {

    private final StudentRepository studentRepository;
    private final ObjectMapper objectMapper;
    private final OtpUtil otpUtil;
    private final EmailUtil emailUtil;
    private final FileUtil fileUtil;

    @Value("${project.profile-pic}")
    private String path;

    @Value("${base.url}")
    private String baseUrl;

    public StudentServiceImp(StudentRepository studentRepository, ObjectMapper objectMapper, OtpUtil otpUtil, EmailUtil emailUtil, FileUtil fileUtil) {
        this.studentRepository = studentRepository;
        this.objectMapper = objectMapper;
        this.otpUtil = otpUtil;
        this.emailUtil = emailUtil;
        this.fileUtil = fileUtil;
    }

    @Override
    public StudentDto register(StudentDto studentDto, MultipartFile file) throws IOException {

        if (Files.exists(Paths.get(path + File.separator + file.getOriginalFilename()))) {
            throw new FileAlreadyExistsException("File already exists! Please enter file name!");
        }

        // Check if a Student with the same email already exists
        if (studentRepository.existsByEmail(studentDto.getEmail())) {
            throw new RuntimeException("User with email " + studentDto.getEmail() + " already exists");
        }

        StringBuilder otp = otpUtil.generateOtp();
        String otpOutput = otp.toString();

        try {
            emailUtil.sendOtpEmail(studentDto.getEmail(), otpOutput);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send OTP to email " + studentDto.getEmail() + " please try again");
        }

        String uploadFileName = fileUtil.uploadFile(path, file);

        String posterUrl = baseUrl + "/api/v1/student/download-pic?fileName=" + uploadFileName;

        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setEmail(studentDto.getEmail());
        studentEntity.setPassword(studentDto.getPassword());
        studentEntity.setName(studentDto.getName());
        studentEntity.setPhoto(uploadFileName);
        studentEntity.setPhone_No(studentDto.getPhone_No());

        OtpVerificationEntity otpVerificationEntity = new OtpVerificationEntity();
        otpVerificationEntity.setOtp(otpOutput);
        otpVerificationEntity.setStatus("NotVerified");
        otpVerificationEntity.setSendTime(LocalDateTime.now());

        // set both side of the relationship
        otpVerificationEntity.setPersonEntity(studentEntity);
        studentEntity.setOtpverification(otpVerificationEntity);

        // Save the studentEntity which should cascade and save the OtpVerificationEntity
        studentRepository.save(studentEntity);

        studentDto.setPhoto(uploadFileName);
        studentDto.setPhotoUrl(posterUrl);

        return studentDto;

    }


    @Override
    public String verifyAccount(String email, String otp) {
        StudentEntity studentEntity = studentRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User with email " + email + "' does nto exist"));
        if (studentEntity != null && studentEntity.getOtpverification().getOtp().equals(otp)
                && Duration.between(studentEntity.getOtpverification().getSendTime(), LocalDateTime.now()).getSeconds() <= (60)) {

            studentEntity.getOtpverification().setStatus("Verified");
            studentRepository.save(studentEntity);
            return "Otp verified you con login";
        } else {
            return "Please regenerate Otp and try again";
        }

    }


    @Override
    public String regenerateOtp(String email) {
        StudentEntity studentEntity = studentRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User with email " + email + "' does not exist"));
        if (studentEntity != null) {
            StringBuilder otp = otpUtil.generateOtp();
            String otpOutput = otp.toString();

            try {
                emailUtil.sendOtpEmail(email, otpOutput);
            } catch (MessagingException e) {
                throw new RuntimeException("Unable to send Otp to email please try again");
            }

            OtpVerificationEntity otpVerificationEntity = studentEntity.getOtpverification();
            otpVerificationEntity.setOtp(otpOutput);
            otpVerificationEntity.setSendTime(LocalDateTime.now());
            studentRepository.save(studentEntity);
            return "Email send successfully please verify your Account within One minute";

        } else {
            return "Failed to send  Email";
        }

    }

    @Override
    public String forgetPassword(String email) {
        StudentEntity studentEntity = studentRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User with email " + email + "' does not exist"));

        if (studentEntity != null) {
            StringBuilder otp = otpUtil.generateOtp();
            String otpOutput = otp.toString();

            OtpVerificationEntity otpVerificationEntity = studentEntity.getOtpverification();
            otpVerificationEntity.setOtp(otpOutput);
            otpVerificationEntity.setSendTime(LocalDateTime.now());
            studentRepository.save(studentEntity);


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
        StudentEntity studentEntity = studentRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User with email " + email + "' does not exist"));
        if (studentEntity != null) {
            studentEntity.setPassword(password);
            studentRepository.save(studentEntity);
            return "Password set successfully";
        } else {
            return "Failed to set Password";
        }

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        StudentEntity studentEntity = studentRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found with this email '" + email + "' "));

        return new StudentPrincipal(studentEntity);
    }
}
