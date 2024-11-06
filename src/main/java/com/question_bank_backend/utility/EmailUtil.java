package com.question_bank_backend.utility;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;


@Component
public class EmailUtil {

    private final JavaMailSender mailSender;


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


    public void setPasswordEmail(String email, String otp) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Reset Password");

       /* mimeMessageHelper.setText("Hello : Your OTP is: " + otp, true);
        mimeMessageHelper.setText("Please regenerate your password within One Minute ", true);*/

        // Set formatted HTML content
        String emailContent = "<html>"
                + "<body>"
                + "<p>Hello,</p>"
                + "<p>Your OTP is: <strong>" + otp + "</strong></p>"
                + "<p>Please regenerate your password within <strong>One Minute</strong>.</p>"
                + "</body>"
                + "</html>";

// Enable HTML content with `true`
        mimeMessageHelper.setText(emailContent, true);

      /*  mimeMessageHelper.setText("""
                <div>
                  <a href="http://localhost:8000/api/v1/superAdmin?email=%s" target="_blank">click link to set Password</a>
                </div>
                """.formatted(email), true);*/

        mailSender.send(mimeMessage);

    }


}
