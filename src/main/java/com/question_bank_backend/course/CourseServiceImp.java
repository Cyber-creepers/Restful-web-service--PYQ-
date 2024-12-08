package com.question_bank_backend.course;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.question_bank_backend.exceptions.AlreadyExistsException;
import com.question_bank_backend.exceptions.CourseNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImp implements CourseService {

    private final CourseRepository courseRepository;

    private final ObjectMapper objectMapper;

    CourseServiceImp(CourseRepository courseRepository, ObjectMapper objectMapper) {
        this.courseRepository = courseRepository;
        this.objectMapper = objectMapper;
    }


    @Override
    public CourseEntity addCourse(CourseDto courseDto) {
        CourseEntity courseEntity = objectMapper.convertValue(courseDto, CourseEntity.class);
        return Optional.of(courseEntity).filter(c -> !courseRepository.existsByCourseFullName(c.getCourseFullName()))
                .map(courseRepository::save)
                .orElseThrow(() -> new AlreadyExistsException(courseDto.getCourseFullName() + " already exists!"));
    }

    @Override
    public CourseEntity updateCourseByCourseId(CourseDto courseDto, String courseId) {
        return Optional.ofNullable(getCourseByCoursedId(courseId)).map(oldCourse -> {
            oldCourse.setCourseFullName(courseDto.getCourseFullName());
            oldCourse.setCourseShortName(courseDto.getCourseShortName());
            return courseRepository.save(oldCourse);
        }).orElseThrow(() -> new CourseNotFoundException("Course not found!"));
    }

    @Override
    public void deleteCourseByCourseId(String courseId) {
        courseRepository.findById(courseId).ifPresentOrElse(courseRepository::delete, () -> {
            throw new CourseNotFoundException("Course not found!");
        });

    }

    @Override
    public List<CourseEntity> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public CourseEntity getCourseByCoursedId(String courseId) {
        return courseRepository.findById(courseId).orElseThrow(() -> new CourseNotFoundException("Course not found! "));
    }
}
