package com.question_bank_backend.subject;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "${api.prefix}/subjects")
public class SubjectController {


    private final SubjectService subjectservice;

    public SubjectController(SubjectService subjectService) {
        this.subjectservice = subjectService;
    }







}
