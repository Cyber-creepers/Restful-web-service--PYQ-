package com.question_bank_backend.admin;


import com.question_bank_backend.utility.MyResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/admin")
public class AdminController {

    AdminService adminService;

    AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping(path = "/register")
    public ResponseEntity<Object> register(@RequestBody AdminDto adminDto) {
        AdminDto registerAdmin = adminService.register(adminDto);
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
        AdminDto adminDto = adminService.login(email, password);
        if (adminDto != null) {
            return MyResponseHandler.generateResponse(HttpStatus.OK, false, "Admin login successful", adminDto);
        } else {
            return MyResponseHandler.generateResponse(HttpStatus.BAD_REQUEST, true, "Admin login failed", null);
        }
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

}
