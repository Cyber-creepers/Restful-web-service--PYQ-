package com.question_bank_backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SuperAdminAuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    public SuperAdminAuthenticationProvider(@Qualifier("superAdminUserDetailsService") UserDetailsService superAdminUserDetailsService) {
        setUserDetailsService(superAdminUserDetailsService);
        setPasswordEncoder(new BCryptPasswordEncoder(12));
    }

}
