package com.question_bank_backend.semester;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SemesterRepository extends JpaRepository<SemesterEntity, String> {

    List<SemesterEntity> findByCourseCourseId(String courseId);

    List<SemesterEntity> findByCourseCourseFullName(String courseFullName);

    List<SemesterEntity> findByCourseCourseShortName(String courseShortName);

    SemesterEntity findBySemesterId(String semesterId);


}
