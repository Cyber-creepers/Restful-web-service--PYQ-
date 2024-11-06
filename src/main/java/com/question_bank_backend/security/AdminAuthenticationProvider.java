package com.question_bank_backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class AdminAuthenticationProvider extends DaoAuthenticationProvider {


    @Autowired
    public AdminAuthenticationProvider(@Qualifier("adminUserDetailsService") UserDetailsService adminUserDetailsService) {
        setUserDetailsService(adminUserDetailsService);
        setPasswordEncoder(new BCryptPasswordEncoder(12));

    }

}
