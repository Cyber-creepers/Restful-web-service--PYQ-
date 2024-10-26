package com.question_bank_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class QuestionBankBackendApplication {

    public static void main(String[] args) {

        SpringApplication.run(QuestionBankBackendApplication.class, args);
        System.out.println("welocme to spring boot application");

        System.out.println("Table creation is happening");
    }

}
