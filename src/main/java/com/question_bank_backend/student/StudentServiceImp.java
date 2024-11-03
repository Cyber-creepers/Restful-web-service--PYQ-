package com.question_bank_backend.student;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.question_bank_backend.otpverification.OtpVerificationEntity;
import com.question_bank_backend.utility.EmailUtil;
import com.question_bank_backend.utility.OtpUtil;
import jakarta.mail.MessagingException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service("studentUserDetailsService")
public class StudentServiceImp implements StudentService {

    StudentRepository studentRepository;
    ObjectMapper objectMapper;
    OtpUtil otpUtil;
    EmailUtil emailUtil;

    StudentServiceImp(StudentRepository studentRepository, ObjectMapper objectMapper, OtpUtil otpUtil, EmailUtil emailUtil) {
        this.studentRepository = studentRepository;
        this.objectMapper = objectMapper;
        this.otpUtil = otpUtil;
        this.emailUtil = emailUtil;
    }

    @Override
    public StudentDto register(StudentDto studentDto) {
        if (studentRepository.findByEmail(studentDto.getEmail()).isPresent()) {
            throw new RuntimeException("User with email " + studentDto.getEmail() + " already exists");
        }

        StringBuilder otp = otpUtil.generateOtp();
        String otpOutput = otp.toString();

        try {
            emailUtil.sendOtpEmail(studentDto.getEmail(), otpOutput);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send OTP to email " + studentDto.getEmail() + " please try again");
        }

        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setEmail(studentDto.getEmail());
        studentEntity.setPassword(studentDto.getPassword());
        studentEntity.setName(studentDto.getName());
        studentEntity.setPhoto(studentDto.getPhoto());
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

        return studentDto;

    }

    @Override
    public StudentDto login(String email, String password) {
        StudentEntity studentEntity = studentRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User with email " + email + " does not exist"));
        if (!studentEntity.getPassword().equals(password)) {
            throw new RuntimeException("Wrong password");
        } else if (!studentEntity.getOtpverification().getStatus().equals("Verified")) {
            throw new RuntimeException("The user with the email address '" + email + "' has not completed OTP verification.");
        }

        StudentDto studentDto = new StudentDto();
        studentDto.setName(studentEntity.getName());
        studentDto.setEmail(studentEntity.getEmail());
        studentDto.setPhone_No(studentEntity.getPhone_No());
        studentDto.setPhoto(studentEntity.getPhoto());

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

      /*  try {
            emailUtil.setPasswordEmail(email);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send email please try again");
        }*/

        return "Please check your email to set new Password";

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
        StudentEntity studentEntity= studentRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User not found with this email '"+ email + "' "));

        return new StudentPrincipal(studentEntity);
    }
}
