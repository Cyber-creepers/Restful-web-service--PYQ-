package com.question_bank_backend.superadmin;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.question_bank_backend.jwt.JwtUtils;
import com.question_bank_backend.utility.FileUtil;
import com.question_bank_backend.utility.MyResponseHandler;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/v1/superAdmin")
public class SuperAdminController {


    private final JwtUtils jwtUtils;

    private final SuperAdminService superAdminService;

    private final ObjectMapper objectMapper;

    private final AuthenticationManager authenticationManager;

    private final FileUtil fileUtil;


    public SuperAdminController(SuperAdminService superAdminService, AuthenticationManager authenticationManager, JwtUtils jwtUtils, ObjectMapper objectMapper, FileUtil fileUtil) {
        this.superAdminService = superAdminService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.objectMapper = objectMapper;
        this.fileUtil = fileUtil;
    }

    @Value("${project.profile-pic}")
    private String profilePic;

    @PostMapping(path = "/register")
    public ResponseEntity<Object> register(@RequestPart MultipartFile file, @RequestPart String superAdminDto) throws IOException {

        if (file.isEmpty()) {
            throw new FileNotFoundException("File is empty! Please send another file!");
        }

        SuperAdminDto registerSuperAdmin = superAdminService.register(convertToSuperAdminDto(superAdminDto), file);

        if (registerSuperAdmin != null) {
            return MyResponseHandler.generateResponse(HttpStatus.CREATED, false, "Super Admin Created Successfully", registerSuperAdmin);
        } else {
            return MyResponseHandler.generateResponse(HttpStatus.BAD_REQUEST, true, "Super Admin Creation Fail", null);
        }
    }


    @PutMapping(path = "/verifyAccount")
    public ResponseEntity<Object> verifyAccount(@RequestParam String email, @RequestParam String otp) {

        String message = superAdminService.verifyAccount(email, otp);

        if (message != null) {
            return MyResponseHandler.generateResponse(HttpStatus.ACCEPTED, false, message, null);
        } else {
            return MyResponseHandler.generateResponse(HttpStatus.BAD_REQUEST, true, message, null);
        }
    }

    @PutMapping(path = "/regenerate-otp")
    public ResponseEntity<Object> regenerateOtp(@RequestParam String email) {
        String message = superAdminService.regenerateOtp(email);
        if (message != null) {
            return MyResponseHandler.generateResponse(HttpStatus.ACCEPTED, false, message, null);
        } else {
            return MyResponseHandler.generateResponse(HttpStatus.BAD_GATEWAY, false, message, null);
        }
    }


    @PutMapping(path = "/login")
    public ResponseEntity<Object> login(@RequestParam String email, @RequestParam String password) {

        // Check if user exists by loading UserDetails
        UserDetails userDetails;

        try {
            userDetails = superAdminService.loadUserByUsername(email);

            // Check if user has completed OTP verification
            if (userDetails != null && ! userDetails.isEnabled()) {
                return MyResponseHandler.generateResponse(HttpStatus.FORBIDDEN, true, "OTP verification is not complete", null);
            }

        } catch (UsernameNotFoundException e) {
            // Return a relevant message if the user is not found
            return MyResponseHandler.generateResponse(HttpStatus.NOT_FOUND, true, "User with this email not found", null);
        }

        // Attempt authentication and handle wrong password case
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));



        } catch (BadCredentialsException e) {
            // Return a relevant message if the password is incorrect
            return MyResponseHandler.generateResponse(HttpStatus.UNAUTHORIZED, true, "Incorrect password", null);
        }

        catch (AuthenticationException e) {
            // Return a generic authentication error message
            return MyResponseHandler.generateResponse(HttpStatus.UNAUTHORIZED, true, "Authentication  failed", null);
        }

        // Set authentication context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token and prepare response
        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);
        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());
        SuperAdminLoginResponse superAdminLoginResponse = new SuperAdminLoginResponse(jwtToken, userDetails.getUsername(), roles);

        return MyResponseHandler.generateResponse(HttpStatus.OK, false, userDetails.getUsername() + " : Login Success", superAdminLoginResponse);
    }


    @PutMapping(path = "/forget-password")
    public ResponseEntity<Object> forgetPassword(@RequestParam String email) {
        String message = superAdminService.forgetPassword(email);
        if (message != null) {
            return MyResponseHandler.generateResponse(HttpStatus.OK, false, message, null);
        } else {
            return MyResponseHandler.generateResponse(HttpStatus.BAD_REQUEST, true, null, null);
        }
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PutMapping(path = "/set-password")
    public ResponseEntity<Object> setPassword(@RequestParam String email, @RequestHeader String password) {

        System.out.println("set password is called ");
        String message = superAdminService.setPassword(email, password);
        if (message != null) {
            return MyResponseHandler.generateResponse(HttpStatus.ACCEPTED, false, message, null);
        } else {
            return MyResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, true, null, null);
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<Object> changePassword(@RequestHeader String otp, @RequestHeader String newPassword, @RequestHeader String email) {
        String message = superAdminService.changePassword(otp, newPassword, email);
        if (message != null) {
            return MyResponseHandler.generateResponse(HttpStatus.ACCEPTED, false, message, null);
        } else {
            return MyResponseHandler.generateResponse(HttpStatus.BAD_REQUEST, true, null, null);
        }
    }


    @GetMapping("/download-pic")
    public void serveFileHandler(HttpServletResponse response, @RequestParam String fileName) throws IOException {
        InputStream resourceFile = fileUtil.getResourceFile(profilePic, fileName);
        if (fileName.endsWith(".pdf")) {
            response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        } else if (fileName.endsWith(".png")) {
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
        } else {
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE); // Default for unknown types
        }
        StreamUtils.copy(resourceFile, response.getOutputStream());

    }


    private SuperAdminDto convertToSuperAdminDto(String superAdminDto) throws JsonProcessingException {

        return objectMapper.readValue(superAdminDto, SuperAdminDto.class);
    }


}
