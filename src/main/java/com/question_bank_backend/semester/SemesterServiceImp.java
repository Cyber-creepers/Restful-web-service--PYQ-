package com.question_bank_backend.semester;


import com.question_bank_backend.course.CourseEntity;
import com.question_bank_backend.course.CourseRepository;
import com.question_bank_backend.exceptions.SemesterNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SemesterServiceImp implements SemesterService {

    private final SemesterRepository semesterRepository;
    private final CourseRepository courseRepository;


    public SemesterServiceImp(SemesterRepository semesterRepository, CourseRepository courseRepository) {
        this.semesterRepository = semesterRepository;
        this.courseRepository = courseRepository;
    }


    @Override
    public SemesterEntity getSemesterBySemesterId(String semesterId) {
        return semesterRepository.findById(semesterId).orElseThrow(() -> new SemesterNotFoundException("Semester not found!"));
    }

    @Override
    public void deleteSemesterBySemesterId(String semesterId) {
        semesterRepository.findById(semesterId).ifPresentOrElse(semesterRepository::delete, () -> {
            throw new SemesterNotFoundException("Semester not Found!");
        });

    }

    @Override
    public SemesterEntity addSemester(SemesterDto semesterDto) {

        CourseEntity courseEntity = Optional.ofNullable(courseRepository.findByCourseFullName(semesterDto.getCourse().getCourseFullName()))
                .orElseGet(() -> {
                    CourseEntity newCourse = new CourseEntity(semesterDto.getCourse().getCourseFullName(), semesterDto.getCourse().getCourseShortName());
                    return courseRepository.save(newCourse);
                });
        semesterDto.setCourse(courseEntity);
        return semesterRepository.save(createSemester(semesterDto, courseEntity));
    }

    private SemesterEntity createSemester(SemesterDto request, CourseEntity courseEntity) {
        return new SemesterEntity(
                request.getSemester(),
                courseEntity
        );
    }

    @Override
    public SemesterEntity updateSemesterBySemesterId(SemesterDto semesterDto, String semesterId) {
        return semesterRepository.findById(semesterId).
                map(existingSemester -> updateExistingSemester(existingSemester, semesterDto))
                .map(semesterRepository::save)
                .orElseThrow(() -> new SemesterNotFoundException("Semester not found!"));
    }

    private SemesterEntity updateExistingSemester(SemesterEntity existingSemester, SemesterDto semesterDto) {
        existingSemester.setSemester(semesterDto.getSemester());

        CourseEntity courseEntity = courseRepository.findByCourseId(semesterDto.getCourse().getCourseId());
        existingSemester.setCourse(courseEntity);
        return existingSemester;
    }

    @Override
    public List<SemesterEntity> getAllSemestersByCourseId(String courseId) {
        return semesterRepository.findByCourseCourseId(courseId);
    }

    @Override
    public List<SemesterEntity> getAllSemestersByCourseFullName(String courseFullName) {
        return semesterRepository.findByCourseCourseFullName(courseFullName);
    }

    @Override
    public List<SemesterEntity> getAllSemestersByCourseShortName(String courseShortName) {
        return semesterRepository.findByCourseCourseShortName(courseShortName);
    }
}
