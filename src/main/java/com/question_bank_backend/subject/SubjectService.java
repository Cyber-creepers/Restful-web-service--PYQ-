package com.question_bank_backend.subject;

import java.util.List;

public interface SubjectService {

    SubjectEntity addSubject(SubjectDto subjectDto);

    SubjectEntity getSubjectById(String subjectId);

    void deleteSubjectById(String subjectId);

    SubjectEntity updateSubject(SubjectDto subjectDto, String subjectId);

    List<SubjectEntity> getAllSubjects();

    List<SubjectEntity> getSubjectsBySubjectName(String subjectName);

    List<SubjectEntity> getSubjectsBySubjectCodeAndSubjectName(String subjectCode, String subjectName);

    SubjectEntity getSubjectsBySubjectCode(String subjectCode);

    List<SubjectEntity> getSubjectsBySubjectCodeAndSemester(String subjectCode, int sem);

    List<SubjectEntity> getSubjectsBySemesterAndCourseShortName(int sem, String courseShortName);

    List<SubjectEntity> getSubjectsBySemesterAndCourseFullName(int sem, String courseFullName);

    Long countSubjectsBySemesterAndCourseShortName(int sem, String courseShortName);


}
