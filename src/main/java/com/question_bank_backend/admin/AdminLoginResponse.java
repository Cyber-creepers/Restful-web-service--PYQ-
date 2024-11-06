package com.question_bank_backend.admin;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AdminLoginResponse {

    private String jwtToken;

    private String username;

    private List<String> roles;


}
