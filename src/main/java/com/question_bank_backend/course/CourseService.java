package com.question_bank_backend.course;

import java.util.List;

public interface CourseService
{
    CourseEntity addCourse(CourseDto courseDto);

    CourseEntity updateCourseByCourseId(CourseDto courseDto, String courseId);

    String deleteCourseByCourseId(String courseId);

    List<CourseEntity> getAllCourses();

    CourseEntity getCourseByCoursedId(String courseId);

}
