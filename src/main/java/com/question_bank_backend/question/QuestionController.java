package com.question_bank_backend.question;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "")
public class QuestionController
{

    private QuestionService questionService;

    QuestionController(QuestionService questionService){
        this.questionService=questionService;
    }

}
