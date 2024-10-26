package com.question_bank_backend.superadmin;


import com.question_bank_backend.jwt.JwtUtils;
import com.question_bank_backend.utility.MyResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/v1/superAdmin")
public class SuperAdminController {


     JwtUtils jwtUtils;
    SuperAdminService superAdminService;

    AuthenticationManager authenticationManager;

    public SuperAdminController(SuperAdminService superAdminService, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.superAdminService = superAdminService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }


    @PostMapping(path = "/register")
    public ResponseEntity<Object> register(@RequestBody SuperAdminDto superAdminDto) {

        SuperAdminDto registerSuperAdmin = superAdminService.register(superAdminDto);

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
      /*  SuperAdminDto superAdminDto = superAdminService.login(email, password);

        if (superAdminDto != null) {
            return MyResponseHandler.generateResponse(HttpStatus.ACCEPTED, false, "Super Admin Login Successfully", superAdminDto);
        } else {
            return MyResponseHandler.generateResponse(HttpStatus.NOT_EXTENDED, true, "Super Admin Login Failed", null);
        }*/

        Authentication authentication;


        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        }catch (AuthenticationException e){
            return MyResponseHandler.generateResponse(HttpStatus.NOT_FOUND,true,e.getMessage(),null);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails= (UserDetails) authentication.getPrincipal();

        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());

        SuperAdminLoginResponse superAdminLoginResponse = new SuperAdminLoginResponse(jwtToken, userDetails.getUsername(), roles);

        return MyResponseHandler.generateResponse(HttpStatus.OK,false, userDetails.getUsername() +" : Login Success",superAdminLoginResponse);




    }

    @PutMapping(path = "/forget-password")
    public ResponseEntity<Object> forgetPassword(@RequestParam String email) {
        String message = superAdminService.forgetPassword(email);
        if (message != null) {
            return MyResponseHandler.generateResponse(HttpStatus.OK, false, message, null);
        } else {
            return MyResponseHandler.generateResponse(HttpStatus.BAD_REQUEST, true, message, null);
        }
    }

    @PutMapping(path = "/set-password")
    public ResponseEntity<Object> setPassword(@RequestParam String email, @RequestHeader String password) {

        System.out.println("set password is called ");
        String message = superAdminService.setPassword(email, password);
        if (message != null) {
            return MyResponseHandler.generateResponse(HttpStatus.ACCEPTED, false, message, null);
        } else {
            return MyResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, true, message, null);
        }
    }


}
