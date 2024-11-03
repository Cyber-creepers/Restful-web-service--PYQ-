package com.question_bank_backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class StudentAuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    public StudentAuthenticationProvider(@Qualifier("studentUserDetailsService") UserDetailsService studentUserDetailsService) {
        setUserDetailsService(studentUserDetailsService);
        setPasswordEncoder(new BCryptPasswordEncoder(12));
    }

}
