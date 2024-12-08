package com.question_bank_backend.subject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SubjectRepository extends JpaRepository<SubjectEntity, String> {


    SubjectEntity findBySubjectCode(String subjectCode);


    List<SubjectEntity> findBySubjectName(String subjectName);


    List<SubjectEntity> findBySubjectNameAndSubjectCode(String subjectName, String subjectCode);

    List<SubjectEntity> findBySubjectCodeAndSemesterSemester(String subjectCode, int semester);


    List<SubjectEntity> findBySemesterSemesterAndSemesterCourseCourseShortName(int semester, String courseShortName);


    List<SubjectEntity> findBySemesterSemesterAndSemesterCourseCourseFullName(int sem, String courseFullName);


    Long countBySemesterSemesterAndSemesterCourseCourseShortName(int sem, String courseShortName);

}