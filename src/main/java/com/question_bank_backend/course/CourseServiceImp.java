package com.question_bank_backend.course;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImp implements CourseService
{

    private final CourseRepository courseRepository;
    private final ObjectMapper objectMapper;

    CourseServiceImp(CourseRepository courseRepository, ObjectMapper objectMapper){
        this.courseRepository =courseRepository;
        this.objectMapper = objectMapper;
    }



}
