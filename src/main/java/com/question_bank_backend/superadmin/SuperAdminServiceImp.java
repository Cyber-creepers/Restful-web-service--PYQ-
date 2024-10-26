package com.question_bank_backend.superadmin;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.question_bank_backend.otpverification.OtpVerificationEntity;
import com.question_bank_backend.utility.EmailUtil;
import com.question_bank_backend.utility.OtpUtil;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class SuperAdminServiceImp implements SuperAdminService {

    SuperAdminRepository superAdminRepository;

    BCryptPasswordEncoder bCryptPasswordEncoder ;

    ObjectMapper objectMapper;

    OtpUtil otpUtil;

    EmailUtil emailUtil;


    @Lazy
    @Autowired
    AuthenticationManager authenticationManager;

    public SuperAdminServiceImp( SuperAdminRepository superAdminRepository, BCryptPasswordEncoder bCryptPasswordEncoder, ObjectMapper objectMapper, OtpUtil otpUtil, EmailUtil emailUtil) {
        this.superAdminRepository = superAdminRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.objectMapper = objectMapper;
        this.otpUtil = otpUtil;
        this.emailUtil = emailUtil;
    }

    @Override
    public SuperAdminDto register(SuperAdminDto superAdminDto) {

        // Check if a SuperAdmin with the same email already exists
        if (superAdminRepository.findByEmail(superAdminDto.getEmail()).isPresent()) {
            throw new RuntimeException("User with email " + superAdminDto.getEmail() + " already exists");
        }


        StringBuilder otp = otpUtil.generateOtp();

        String otpOutput = otp.toString();

        try {
            emailUtil.sendOtpEmail(superAdminDto.getEmail(), otpOutput);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send otp to email please try again");
        }

        SuperAdminEntity superAdminEntity = new SuperAdminEntity();
        superAdminEntity.setEmail(superAdminDto.getEmail());
        superAdminEntity.setPassword(bCryptPasswordEncoder.encode(superAdminDto.getPassword()));
        superAdminEntity.setName(superAdminDto.getName());
        superAdminEntity.setPhoto(superAdminDto.getPhoto());
        superAdminEntity.setPhone_No(superAdminDto.getPhone_No());

        OtpVerificationEntity otpVerificationEntity = new OtpVerificationEntity();
        otpVerificationEntity.setOtp(otpOutput);
        otpVerificationEntity.setSendTime(LocalDateTime.now());
        otpVerificationEntity.setStatus("NotVerified");

        // Set both sides of the relationship
        otpVerificationEntity.setPersonEntity(superAdminEntity);
        superAdminEntity.setOtpverification(otpVerificationEntity);

        // Save the SuperAdminEntity which should cascade and save the OtpVerificationEntity
        superAdminRepository.save(superAdminEntity);


        return superAdminDto;
    }


    @Override
    public String verifyAccount(String email, String otp) {
        SuperAdminEntity superAdminEntity = superAdminRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found with this email " + email));
        if (superAdminEntity != null && superAdminEntity.getOtpverification().getOtp().equals(otp)
                && Duration.between(superAdminEntity.getOtpverification().getSendTime(), LocalDateTime.now()).getSeconds() < (60)) {


            superAdminEntity.getOtpverification().setStatus("Verified");
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

            OtpVerificationEntity otpVerificationEntity = superAdminEntity.getOtpverification();
            otpVerificationEntity.setOtp(otpOutput);
            otpVerificationEntity.setSendTime(LocalDateTime.now());
            superAdminRepository.save(superAdminEntity);
            return "Email send Successfully please verify Account within One minute ";

        } else {
            return "Failed to send Email ";
        }
    }

    @Override
    public SuperAdminDto login(String email, String password) {
  /*    Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

      if(authentication.isAuthenticated()){
          return null;
      }

       return  null;*/



        /*SuperAdminEntity superAdminEntity = superAdminRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found with this email " + email));

        if (!superAdminEntity.getPassword().equals(password)) {
            throw new RuntimeException("Wrong password");
        } else if (!superAdminEntity.getOtpverification().getStatus().equals("Verified")) {
            throw new RuntimeException("The user with the email " + email + " ' has not completed OTP verification");
        }

        SuperAdminDto dto = new SuperAdminDto();
        dto.setName(superAdminEntity.getName());
        dto.setEmail(superAdminEntity.getEmail());
        dto.setPhone_No(superAdminEntity.getPhone_No());
        dto.setPhoto(superAdminEntity.getPhoto());
        return dto;*/
        return  null;
    }

    @Override
    public String forgetPassword(String email) {

        SuperAdminEntity superaDminEntity = superAdminRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found with this email " + email));


        try {
            emailUtil.setPasswordEmail(email);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send email please try again");
        }


        return "Please check your email to set new Password";
    }

    @Override
    public String setPassword(String email, String password) {
        SuperAdminEntity superAdminEntity = superAdminRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found with this email " + email));
        if (superAdminEntity != null) {
            superAdminEntity.setPassword(password);
            superAdminRepository.save(superAdminEntity);
            return "Password set successfully";
        } else {
            return "Failed to set password";
        }
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       SuperAdminEntity superAdminEntity= superAdminRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User not found with this email '"+email+"' "));

        return new SuperAdminPrincipal(superAdminEntity);
    }


}
