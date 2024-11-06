package com.question_bank_backend.admin;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.question_bank_backend.jwt.JwtUtils;
import com.question_bank_backend.utility.FileUtil;
import com.question_bank_backend.utility.MyResponseHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
            return MyResponseHandler.generateResponse(HttpStatus.CREATED, false, "Admin register successfully", registerAdmin);
        } else {
            return MyResponseHandler.generateResponse(HttpStatus.BAD_REQUEST, true, "Admin registration failed", null);
        }
    }

    @PutMapping(path = "/verify-account")
    public ResponseEntity<Object> verifyAccount(@RequestParam String email, @RequestHeader String otp) {

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

        // Check if user exists by loading UserDetails
        UserDetails userDetails;

        try {
            userDetails = adminService.loadUserByUsername(email);

            // Check if user has completed OTP verification
            if (userDetails != null && !userDetails.isEnabled()) {
                return MyResponseHandler.generateResponse(HttpStatus.FORBIDDEN, true, "OTP verification is not completed", null);
            }

        } catch (UsernameNotFoundException e) {
            // Return a relevant message if the user is not found
            return MyResponseHandler.generateResponse(HttpStatus.NOT_FOUND, true, "User with this email does not exist", null);
        }

        // Attempt authentication and handle wrong password case
        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (BadCredentialsException e) {
            // Return a relevant message of the password is incorrect
            return MyResponseHandler.generateResponse(HttpStatus.UNAUTHORIZED, true, "Incorrect password", null);
        } catch (AuthenticationException e) {
            // Return a generic authentication error message
            return MyResponseHandler.generateResponse(HttpStatus.UNAUTHORIZED, true, "Authentication failed", null);
        }


        // Set authentication context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token and prepare response
        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);
        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());
        AdminLoginResponse adminLoginResponse = new AdminLoginResponse(jwtToken, userDetails.getUsername(), roles);

        return MyResponseHandler.generateResponse(HttpStatus.OK, false, userDetails.getUsername() + " : Login Success", adminLoginResponse);
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

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/set-password")
    public ResponseEntity<Object> setPassword(@RequestParam String email, @RequestHeader String password) {
        String message = adminService.setPassword(email, password);
        if (message != null) {
            return MyResponseHandler.generateResponse(HttpStatus.OK, false, message, null);
        } else {
            return MyResponseHandler.generateResponse(HttpStatus.BAD_REQUEST, true, message, null);
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<Object> changePassword(@RequestHeader String otp, @RequestHeader String newPassword, @RequestHeader String email){
        String message = adminService.changePassword(otp, newPassword, email);

        if (message != null){
            return MyResponseHandler.generateResponse(HttpStatus.ACCEPTED, false, message, null);
        }else{
            return MyResponseHandler.generateResponse(HttpStatus.BAD_REQUEST,true, null,null);
        }
    }




    private AdminDto convertToAdminDto(String adminDto) throws JsonProcessingException {
        return objectMapper.readValue(adminDto, AdminDto.class);

    }

}
