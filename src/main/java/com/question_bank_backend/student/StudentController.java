package com.question_bank_backend.student;


import com.question_bank_backend.utility.MyResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/student")
public class StudentController {

    StudentService studentService;

    StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping(path = "/register")
    public ResponseEntity<Object> register(@RequestBody StudentDto studentDto) {
        StudentDto registerStudent = studentService.register(studentDto);

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
        StudentDto studentDto = studentService.login(email, password);
        if (studentDto != null) {
            return MyResponseHandler.generateResponse(HttpStatus.OK, false, "Student login successfully", studentDto);
        } else {
            return MyResponseHandler.generateResponse(HttpStatus.NOT_EXTENDED, true, "Student login failed", null);
        }
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


}
