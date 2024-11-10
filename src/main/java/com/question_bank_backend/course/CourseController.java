package com.question_bank_backend.course;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "")
public class CourseController {

    private final CourseService courseService;

    CourseController(CourseService courseService){
        this.courseService= courseService;
    }





}


