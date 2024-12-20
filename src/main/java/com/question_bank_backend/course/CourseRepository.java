package com.question_bank_backend.course;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, String> {


    boolean existsByCourseFullName(String courseFullName);

    CourseEntity findByCourseId(String courseId);

    CourseEntity findByCourseFullName(String courseFullName);

}
