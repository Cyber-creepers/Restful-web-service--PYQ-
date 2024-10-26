package com.question_bank_backend.student;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto
{

   private String name;

   private Long phone_No;

   private byte[] photo;

   @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
   private String password;

   private String email;


}
