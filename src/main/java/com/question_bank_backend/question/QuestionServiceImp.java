package com.question_bank_backend.question;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class QuestionServiceImp implements QuestionService
{

    private final QuestionRepository questionRepository;
    private final ObjectMapper objectMapper;

    QuestionServiceImp(QuestionRepository questionRepository,ObjectMapper objectMapper){
        this.questionRepository=questionRepository;
        this.objectMapper=objectMapper;
    }
}
