package com.question_bank_backend.student;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.question_bank_backend.jwt.JwtUtils;
import com.question_bank_backend.utility.FileUtil;
import com.question_bank_backend.utility.MyResponseHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/v1/student")
public class StudentController {

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final StudentService studentService;

    private final ObjectMapper objectMapper;

    private final FileUtil fileUtil;

    @Value("${project.profile-pic}")
    private String profilePic;

    public StudentController(AuthenticationManager authenticationManager, JwtUtils jwtUtils, StudentService studentService, ObjectMapper objectMapper, FileUtil fileUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.studentService = studentService;
        this.objectMapper = objectMapper;
        this.fileUtil = fileUtil;
    }


    @PostMapping(path = "/register")
    public ResponseEntity<Object> register(@RequestPart MultipartFile file,@RequestPart String studentDto) throws IOException {

        if (file.isEmpty()) {
            throw new FileNotFoundException("File is empty! Please send another file!");
        }

        StudentDto registerStudent = studentService.register(convertToStudentDto(studentDto),file);

        if (registerStudent != null) {
            return MyResponseHandler.generateResponse(HttpStatus.CREATED, false, "Student register successfully", registerStudent);
        } else {
            return MyResponseHandler.generateResponse(HttpStatus.BAD_REQUEST, true, "Student registration failed", null);
        }
    }

    @PutMapping(path = "/verify-account")
    public ResponseEntity<Object> verifyAccount(@RequestParam String email, @RequestParam String otp) {
        String message = studentService.verifyAccount(email, otp);
        if (message != null) {
            return MyResponseHandler.generateResponse(HttpStatus.ACCEPTED, false, message, null);
        } else {
            return MyResponseHandler.generateResponse(HttpStatus.BAD_REQUEST, true, message, null);
        }
    }

    @PutMapping(path = "/regenerate-otp")
    public ResponseEntity<Object> regenerateOtp(@RequestParam String email) {
        String message = studentService.regenerateOtp(email);
        if (message != null) {
            return MyResponseHandler.generateResponse(HttpStatus.ACCEPTED, false, message, null);
        } else {
            return MyResponseHandler.generateResponse(HttpStatus.BAD_GATEWAY, true, message, null);
        }
    }

    @PutMapping(path = "/login")
    public ResponseEntity<Object> login(@RequestHeader String email, @RequestHeader String password) {

        // Check if user exists by loading UserDetails
        UserDetails userDetails;

        try {
            userDetails = studentService.loadUserByUsername(email);

            // Check if user has completed OTP verification
            if (userDetails != null && !userDetails.isEnabled()) {
                return MyResponseHandler.generateResponse(HttpStatus.FORBIDDEN, true, "OTP verification is not complete", null);
            }

        } catch (UsernameNotFoundException e) {
            // return a relevant message if the user is not found
            return MyResponseHandler.generateResponse(HttpStatus.NOT_FOUND, true, "User with this email not found", null);
        }

        // Attempt authentication and handle wrong password case
        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (BadCredentialsException e) {
            // Return a relevant message if the password id incorrect
            return MyResponseHandler.generateResponse(HttpStatus.UNAUTHORIZED, true, "Incorrect password", null);
        }
        catch (AuthenticationException e) {
            // Return a generic authentication error message
            return MyResponseHandler.generateResponse(HttpStatus.UNAUTHORIZED, true, "Authentication   failed", null);
        }

        // Set authentication context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token and prepare response
        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);
        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());
        StudentLoginResponse studentLoginResponse = new StudentLoginResponse(jwtToken, userDetails.getUsername(), roles);
        return MyResponseHandler.generateResponse(HttpStatus.OK, false, userDetails.getUsername() + " : Login Success", studentLoginResponse);
    }

    @PutMapping(path = "/forget-password")
    public ResponseEntity<Object> forgetPasswprd(@RequestParam String email) {

        String message = studentService.forgetPassword(email);
        if (message != null) {
            return MyResponseHandler.generateResponse(HttpStatus.OK, false, message, null);
        } else {
            return MyResponseHandler.generateResponse(HttpStatus.BAD_REQUEST, true, message, null);
        }
    }

    @PutMapping(path = "/set-password")
    public ResponseEntity<Object> setPassword(@RequestParam String email, @RequestHeader String password) {
        String message = studentService.setPassword(email, password);
        if (message != null) {
            return MyResponseHandler.generateResponse(HttpStatus.ACCEPTED, false, message, null);
        } else {
            return MyResponseHandler.generateResponse(HttpStatus.NOT_IMPLEMENTED, true, message, null);
        }
    }

    private StudentDto convertToStudentDto(String studentDto) throws JsonProcessingException {
        return objectMapper.readValue(studentDto,StudentDto.class);
    }


}
