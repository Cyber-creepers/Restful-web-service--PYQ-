package com.question_bank_backend.course;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImp implements CourseService
{

    private final CourseRepository courseRepository;
    private final ObjectMapper objectMapper;

    CourseServiceImp(CourseRepository courseRepository, ObjectMapper objectMapper){
        this.courseRepository =courseRepository;
        this.objectMapper = objectMapper;
    }


    @Override
    public CourseEntity addCourse(CourseDto courseDto) {
        return null;
    }

    @Override
    public CourseEntity updateCourseByCourseId(CourseDto courseDto, String courseId) {
        return null;
    }

    @Override
    public String deleteCourseByCourseId(String courseId) {
        return "";
    }

    @Override
    public List<CourseEntity> getAllCourses() {
        return List.of();
    }

    @Override
    public CourseEntity getCourseByCoursedId(String courseId) {
        return null;
    }
}
