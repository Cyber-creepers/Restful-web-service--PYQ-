package com.question_bank_backend.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentLoginResponse {
    private String jwtToken;

    private String username;

    private List<String> roles;
}
