package com.question_bank_backend.jwt;


import com.question_bank_backend.student.StudentRepository;
import com.question_bank_backend.superadmin.SuperAdminRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@NoArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    @Qualifier("studentUserDetailsService")
    private UserDetailsService studentUserDetailsService;

    @Autowired
    @Qualifier("superAdminUserDetailsService")
    private UserDetailsService superAdminUserDetailsService;

    @Autowired
    @Qualifier("adminUserDetailsService")
    private UserDetailsService adminUserDetailsService;

    @Autowired
    private StudentRepository studentRepository;


    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Autowired
    private SuperAdminRepository superAdminRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {


        logger.debug("AuthTokenFilter called for URI: {}", request.getRequestURI());
        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String userEmail = jwtUtils.getUserNameFromJwtToken(jwt);

                UserDetails userDetails;

                if(isStudent(userEmail)){
                    userDetails = studentUserDetailsService.loadUserByUsername(userEmail);
                }else if(isSuperAdmin(userEmail)){
                    userDetails= superAdminUserDetailsService.loadUserByUsername(userEmail);
                }else {
                    userDetails = adminUserDetailsService.loadUserByUsername(userEmail);
                }


                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails,
                                null,
                                userDetails.getAuthorities());
                logger.debug("Roles from JWT: {}", userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {

            logger.error("Cannot set user authentication: {}", e.getMessage());
            throw new ServletException("Cannot set user authentication : {} " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String jwt = jwtUtils.getJwtFromHeader(request);
        logger.debug("AuthTokenFilter.java: {}", jwt);
        return jwt;
    }

    private boolean isStudent(String userEmail){
        return studentRepository.existsByEmail(userEmail);
    }

    private boolean isSuperAdmin(String userEmail){
        return superAdminRepository.existsByEmail(userEmail);
    }


}