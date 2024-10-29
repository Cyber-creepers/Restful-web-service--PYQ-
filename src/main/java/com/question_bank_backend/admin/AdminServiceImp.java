package com.question_bank_backend.admin;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.question_bank_backend.otpverification.OtpVerificationEntity;
import com.question_bank_backend.utility.EmailUtil;
import com.question_bank_backend.utility.OtpUtil;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class AdminServiceImp  implements AdminService
{

     AdminRepository adminRepository;
     ObjectMapper objectMapper;
     OtpUtil otpUtil;
     EmailUtil emailUtil;


    AdminServiceImp(AdminRepository adminRepository, ObjectMapper objectMapper, OtpUtil otpUtil, EmailUtil emailUtil){
        this.otpUtil = otpUtil;
        this.emailUtil = emailUtil;
        this.adminRepository=adminRepository;
        this.objectMapper=objectMapper;
    }

    @Override
    public AdminDto register(AdminDto adminDto)
    {
       if (adminRepository.findByEmail(adminDto.getEmail()).isPresent()){
           throw new RuntimeException("User with email "+adminDto.getEmail()+"' already exists");
       }

       StringBuilder otp= otpUtil.generateOtp();
       String otpOutput=otp.toString();

       try {
           emailUtil.sendOtpEmail(adminDto.getEmail(), otpOutput);
       }catch (MessagingException e){
           throw new RuntimeException("Unable to send OTP to email "+adminDto.getEmail()+"' please try again");
       }

       AdminEntity adminEntity=new AdminEntity();
       adminEntity.setEmail(adminDto.getEmail());
       adminEntity.setPassword(adminDto.getPassword());
       adminEntity.setName(adminDto.getName());
       adminEntity.setVerifiedBy(adminDto.getVerifiedBy());
       adminEntity.setPhoto(adminDto.getPhoto());
       adminEntity.setPhone_No(adminDto.getPhone_NO());

        OtpVerificationEntity otpVerificationEntity =new OtpVerificationEntity();
        otpVerificationEntity.setOtp(otpOutput);
        otpVerificationEntity.setStatus("NotVerified");
        otpVerificationEntity.setSendTime(LocalDateTime.now());

        //set both side of the relationship
        otpVerificationEntity.setPersonEntity(adminEntity);
        adminEntity.setOtpverification(otpVerificationEntity);

        // Save the studentEntity which should cascade and save the OtpVerificationEntity
        adminRepository.save(adminEntity);

        return adminDto;
    }

    @Override
    public AdminDto login(String email, String password)
    {
      AdminEntity adminEntity=  adminRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("User with email "+email+"' do not exist"));

      if(!adminEntity.getPassword().equals(password))
      {
          throw new RuntimeException("Incorrect password");
      }else if(!adminEntity.getOtpverification().getStatus().equals("Verified")) {
          throw new RuntimeException("The user with the email address '"+email+"' has not completed otp verification");
      }

      return objectMapper.convertValue(adminEntity, AdminDto.class);
    }

    @Override
    public String verifyAccount(String email, String otp)
    {
       AdminEntity adminEntity= adminRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("User withe email '"+email+"' do not exist"));
       if (adminEntity != null
               && adminEntity.getOtpverification().getOtp().equals(otp)
               && Duration.between(adminEntity.getOtpverification().getSendTime(),LocalDateTime.now()).getSeconds() <=60) {

           adminEntity.getOtpverification().setStatus("Verified");
           adminRepository.save(adminEntity);
           return "Otp verified you can login";
       }else {
           return "Please regenerate Otp and try again";
       }
    }

    @Override
    public String regenerateOtp(String email)
    {
        AdminEntity adminEntity=adminRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("User with this email '"+email+"' do not exist"));
        if (adminEntity != null){
            StringBuilder otp=otpUtil.generateOtp();
            String otpOutput=otp.toString();

            try {
                emailUtil.sendOtpEmail(email, otpOutput);
            }catch(MessagingException e){
                throw new RuntimeException("Unable to send Otp to email please try again");
            }

            OtpVerificationEntity otpVerificationEntity=adminEntity.getOtpverification();
            otpVerificationEntity.setOtp(otpOutput);
            otpVerificationEntity.setSendTime(LocalDateTime.now());
            adminRepository.save(adminEntity);
            return "Email send successfully please verify your Account within one minute";
        }else{
            return "Failed to send Email";
        }
    }

    @Override
    public String forgetPassword(String email)
    {
      AdminEntity adminEntity=  adminRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("User with this email '"+email+"' do not exist"));

         /* try {
              emailUtil.setPasswordEmail(email);
          }catch (MessagingException e){
              throw new RuntimeException("Unable to send email please try again");
          }*/

       return "Please check your email to set new Password";
    }

    @Override
    public String setPassword(String email, String password)
    {
       AdminEntity adminEntity= adminRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("User with this email '"+email+"' do not exist"));

       if(adminEntity != null){
           adminEntity.setPassword(password);
           adminRepository.save(adminEntity);
           return "Password set successfully";
       }else {
           return "Failed to set Password";
       }

    }
}
