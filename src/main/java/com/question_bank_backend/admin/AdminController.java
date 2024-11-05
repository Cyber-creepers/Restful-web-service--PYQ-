package com.question_bank_backend.admin;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.question_bank_backend.jwt.JwtUtils;
import com.question_bank_backend.utility.FileUtil;
import com.question_bank_backend.utility.MyResponseHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping(path = "/api/v1/admin")
public class AdminController {

    private final AdminService adminService;

    private final JwtUtils jwtUtils;

    private final ObjectMapper objectMapper;

    private final AuthenticationManager authenticationManager;

    private final FileUtil fileUtil;

    public AdminController(AdminService adminService, JwtUtils jwtUtils, ObjectMapper objectMapper, AuthenticationManager authenticationManager, FileUtil fileUtil) {
        this.adminService = adminService;
        this.jwtUtils = jwtUtils;
        this.objectMapper = objectMapper;
        this.authenticationManager = authenticationManager;
        this.fileUtil = fileUtil;
    }

    @Value("${project.profile-pic}")
    private String profilePic;


    @PostMapping(path = "/register")
    public ResponseEntity<Object> register(@RequestPart MultipartFile file, @RequestPart String adminDto) throws IOException {

        if (file.isEmpty()) {
            throw new FileNotFoundException("File is empty! Please send another file!");
        }

        AdminDto registerAdmin = adminService.register(convertToAdminDto(adminDto), file);

        if (registerAdmin != null) {
            return MyResponseHandler.generateResponse(HttpStatus.CREATED, false, "Admin register successfully", adminDto);
        } else {
            return MyResponseHandler.generateResponse(HttpStatus.BAD_REQUEST, true, "Admin registration failed", null);
        }
    }

    @PutMapping(path = "/verify-account")
    public ResponseEntity<Object> verifyAccount(@RequestParam String email, @RequestParam String otp) {
        String message = adminService.verifyAccount(email, otp);
        if (message != null) {
            return MyResponseHandler.generateResponse(HttpStatus.ACCEPTED, false, message, null);
        } else {
            return MyResponseHandler.generateResponse(HttpStatus.BAD_REQUEST, true, message, null);
        }
    }

    @PutMapping(path = "/regenerate-otp")
    public ResponseEntity<Object> regenerateOtp(@RequestParam String email) {
        String message = adminService.regenerateOtp(email);
        if (message != null) {
            return MyResponseHandler.generateResponse(HttpStatus.ACCEPTED, false, message, null);
        } else {
            return MyResponseHandler.generateResponse(HttpStatus.BAD_REQUEST, true, message, null);
        }
    }


    @PutMapping(path = "/login")
    public ResponseEntity<Object> login(@RequestParam String email, @RequestHeader String password) {
        /*//AdminDto adminDto = adminService.login(email, password);
       // if (adminDto != null) {
            return MyResponseHandler.generateResponse(HttpStatus.OK, false, "Admin login successful", adminDto);
        } else {
            return MyResponseHandler.generateResponse(HttpStatus.BAD_REQUEST, true, "Admin login failed", null);
        }*/
        return null;
    }

    @PutMapping(path = "/forget-password")
    public ResponseEntity<Object> forgetPassword(@RequestParam String email) {
        String message = adminService.forgetPassword(email);
        if (message != null) {
            return MyResponseHandler.generateResponse(HttpStatus.OK, false, message, null);
        } else {
            return MyResponseHandler.generateResponse(HttpStatus.BAD_REQUEST, true, message, null);
        }
    }

    @PutMapping(path = "/set-password")
    public ResponseEntity<Object> setPassword(@RequestParam String email, @RequestHeader String password) {
        String message = adminService.setPassword(email, password);
        if (message != null) {
            return MyResponseHandler.generateResponse(HttpStatus.OK, false, message, null);
        } else {
            return MyResponseHandler.generateResponse(HttpStatus.BAD_REQUEST, true, message, null);
        }
    }

    private AdminDto convertToAdminDto(String adminDto) throws JsonProcessingException {
        return objectMapper.readValue(adminDto, AdminDto.class);

    }

}
