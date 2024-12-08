package com.question_bank_backend.semester;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.question_bank_backend.course.CourseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SemesterServiceImp  implements SemesterService
{

    private final SemesterRepository semesterRepository;
    private final ObjectMapper objectMapper;
    private final CourseService courseService;


    public SemesterServiceImp(SemesterRepository semesterRepository, ObjectMapper objectMapper, CourseService courseService) {
        this.semesterRepository = semesterRepository;
        this.objectMapper = objectMapper;
        this.courseService = courseService;
    }


    @Override
    public SemesterEntity getSemesterBySemesterId(String semesterId) {
        return null;
    }

    @Override
    public void deleteSemesterBySemesterId(String semesterId) {

    }

    @Override
    public SemesterEntity createSemester(SemesterDto semesterDto) {
        return null;
    }

    @Override
    public SemesterEntity updateSemesterBySemesterId(SemesterDto semesterDto, String semesterId) {
        return null;
    }

    @Override
    public List<SemesterEntity> getAllSemestersByCourseId(String courseId) {
        return List.of();
    }

    @Override
    public List<SemesterEntity> getAllSemestersByCourseFullName(String courseFullName) {
        return List.of();
    }

    @Override
    public List<SemesterEntity> getAllSemestersByCourseShortName(String courseShortName) {
        return List.of();
    }
}
