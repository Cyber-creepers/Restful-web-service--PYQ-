package com.question_bank_backend.exceptions;


import com.question_bank_backend.utility.MyResponseHandler;
import jakarta.servlet.ServletException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Object> exceptionHandler(FileNotFoundException fileNotFoundException) {

        return MyResponseHandler.generateResponse(HttpStatus.NOT_FOUND, true, fileNotFoundException.getMessage(), null);

    }

    @ExceptionHandler
    public ResponseEntity<Object> exceptionHandler(RuntimeException runtimeException) {
        return MyResponseHandler.generateResponse(HttpStatus.CONFLICT, true, runtimeException.getMessage(), null);
    }

    @ExceptionHandler
    public ResponseEntity<Object> exceptionHandler(FileAlreadyExistsException fileAlreadyExistsException) {
        return MyResponseHandler.generateResponse(HttpStatus.CONFLICT, true, fileAlreadyExistsException.getMessage(), null);
    }

    @ExceptionHandler
    public ResponseEntity<Object> exceptionHandler(MailSendException mailSendException) {
        return MyResponseHandler.generateResponse(HttpStatus.REQUEST_TIMEOUT, true, mailSendException.getMessage(), null);
    }


    @ExceptionHandler(ServletException.class)
    public ResponseEntity<Object> exceptionHandler(ServletException e){
        return MyResponseHandler.generateResponse(HttpStatus.UNAUTHORIZED,true,e.getMessage(),null);
    }


}
