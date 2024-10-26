package com.question_bank_backend.utility;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;


@Component
public class EmailUtil {

    private JavaMailSender mailSender;


    EmailUtil(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    public void sendOtpEmail(String email, String otp) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Verify OTP");
        mimeMessageHelper.setText("Hello : Your OTP is: " + otp, true);
/*
        mimeMessageHelper.setText("""
        <div>
          <a href="http://localhost:8080/verify-account?email=%s&otp=%s" target="_blank">click link to verify</a>
        </div>
        """.formatted(email, otp), true);*/

        mailSender.send(mimeMessage);
    }


    public void setPasswordEmail(String email) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Reset Password");

        // mimeMessageHelper.setText("Hello : Your OTP is: " + otp, true);
        mimeMessageHelper.setText("""
                <div>
                  <a href="http://localhost:8000/api/v1/superAdmin?email=%s" target="_blank">click link to set Password</a>
                </div>
                """.formatted(email), true);

        mailSender.send(mimeMessage);

    }


}
