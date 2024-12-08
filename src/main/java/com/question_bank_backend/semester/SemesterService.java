package com.question_bank_backend.semester;


import java.util.List;

public interface SemesterService
{

    SemesterEntity getSemesterBySemesterId(String  semesterId);

    void deleteSemesterBySemesterId(String semesterId);

    SemesterEntity addSemester(SemesterDto semesterDto);

    SemesterEntity updateSemesterBySemesterId(SemesterDto semesterDto, String semesterId);

    List<SemesterEntity> getAllSemestersByCourseId(String courseId);

    List<SemesterEntity> getAllSemestersByCourseFullName(String courseFullName);

    List<SemesterEntity> getAllSemestersByCourseShortName(String courseShortName);


}
