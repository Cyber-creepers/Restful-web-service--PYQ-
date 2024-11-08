package com.question_bank_backend.config;

import com.question_bank_backend.jwt.AuthEntryPoint;
import com.question_bank_backend.jwt.AuthTokenFilter;
import com.question_bank_backend.security.AdminAuthenticationProvider;
import com.question_bank_backend.security.StudentAuthenticationProvider;
import com.question_bank_backend.security.SuperAdminAuthenticationProvider;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
@Setter
@Getter
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {


    private final DataSource dataSource;
    private final AuthEntryPoint authEntryPoint;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private SuperAdminAuthenticationProvider superAdminAuthenticationProvider;
    @Autowired
    private StudentAuthenticationProvider studentauthenticationProvider;
    @Autowired
    private AdminAuthenticationProvider adminAuthenticationProvider;

    public SecurityConfig(DataSource dataSource, AuthEntryPoint authEntryPoint, BCryptPasswordEncoder bCryptPasswordEncoder) {

        this.dataSource = dataSource;
        this.authEntryPoint = authEntryPoint;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // Custom method to configure AuthenticationManagerBuilder
    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Register custom authentication providers
        auth.authenticationProvider(superAdminAuthenticationProvider);
        auth.authenticationProvider(studentauthenticationProvider);
        auth.authenticationProvider(adminAuthenticationProvider);
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity.
                exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPoint)).
                csrf(customizer -> customizer.disable())
                .authorizeHttpRequests(request -> request.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/actuator/**"
                                , "/api/v1/superAdmin/login", "/api/v1/superAdmin/register"
                                , "/api/v1/superAdmin/verifyAccount", "/api/v1/superAdmin/regenerate-otp"
                                , "/api/v1/superAdmin/forget-password", "/api/v1/superAdmin/change-password",
                                "/api/v1/superAdmin/download-pic" , "/api/v1/student/download-pic" , "/api/v1/student/change-password",
                                "/api/v1/student/forget-password" , "/api/v1/student/login" , "/api/v1/student/regenerate-otp" ,
                                "/api/v1/student/register" , "/api/v1/student/verify-account" , "/api/v1/admin/verify-account" ,
                                 "api/v1/admin/regenerate-otp" , "/api/v1/admin/login" , "/api/v1/admin/forget-password" , "/api/v1/admin/change-password"
                       ,"/api/v1/admin/register"  ).permitAll()
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
