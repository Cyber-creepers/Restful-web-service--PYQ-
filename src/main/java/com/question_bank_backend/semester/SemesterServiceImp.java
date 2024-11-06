package com.question_bank_backend.semester;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class SemesterServiceImp  implements SemesterService
{

    private final SemesterRepository semesterRepository;
    private final ObjectMapper objectMapper;

    SemesterServiceImp(SemesterRepository semesterRepository,ObjectMapper objectMapper)
    {
        this.semesterRepository=semesterRepository;
        this.objectMapper=objectMapper;
    }

}
