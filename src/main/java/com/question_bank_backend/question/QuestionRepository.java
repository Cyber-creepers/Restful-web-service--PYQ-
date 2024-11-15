package com.question_bank_backend.question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<QuestionEntity, String> {


    List<QuestionEntity> findByYear(int year);


    List<QuestionEntity> findBySubjectSubjectCode(String subjectCode);

    List<QuestionEntity> findBySubjectSubjectCodeAndYear(String subjectCode, int year);


    List<QuestionEntity> findBySubjectSubjectNameAndYear(String subjectName, int year);

    List<QuestionEntity> findBySubject_Semester_SemAndSubject_SubjectCode(int semester, String subjectCode);


    List<QuestionEntity> findBySubject_Semester_SemAndSubject_SubjectNameAndYear(int semester, String subjectName, int year);

    List<QuestionEntity> findBySubject_Semester_SemAndSubject_SubjectName(int semester, String subjectName);

    List<QuestionEntity> findBySubject_Semester_Course_CourseFullName(String courseFullName);


    List<QuestionEntity> findBySubject_Semester_Course_CourseShortName(String courseShortName);


}
