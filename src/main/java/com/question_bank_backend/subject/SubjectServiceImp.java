package com.question_bank_backend.subject;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class SubjectServiceImp implements SubjectService
{

    private SubjectRepository subjectRepository;
    private ObjectMapper objectMapper ;

    SubjectServiceImp(SubjectRepository subjectRepository,ObjectMapper objectMapper)
    {
        this.subjectRepository=subjectRepository;
        this.objectMapper=objectMapper;
    }

}
