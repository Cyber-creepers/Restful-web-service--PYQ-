package com.question_bank_backend.subject;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.question_bank_backend.course.CourseEntity;
import com.question_bank_backend.course.CourseRepository;
import com.question_bank_backend.course.CourseService;
import com.question_bank_backend.exceptions.SubjectNotFoundException;
import com.question_bank_backend.semester.SemesterEntity;
import com.question_bank_backend.semester.SemesterRepository;
import com.question_bank_backend.semester.SemesterService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectServiceImp implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final ObjectMapper objectMapper;
    private final SemesterService semesterservice;
    private final SemesterRepository semesterRepository;
    private final CourseService courseService;
    private final CourseRepository courseRepository;

    public SubjectServiceImp(CourseRepository courseRepository, SemesterRepository semesterRepository, SubjectRepository subjectRepository, ObjectMapper objectMapper, SemesterService semesterservice, CourseService courseService) {
        this.courseRepository = courseRepository;
        this.semesterRepository = semesterRepository;
        this.subjectRepository = subjectRepository;
        this.objectMapper = objectMapper;
        this.semesterservice = semesterservice;
        this.courseService = courseService;
    }

    @Override
    public SubjectEntity addSubject(SubjectDto subjectDto) {

       /* CourseEntity courseEntity =Optional.ofNullable(courseRepository.findByCourseId(subjectDto.getSemester().getCourse().getCourseId()))
                .orElseGet(()->{
                    CourseEntity newCourse = new CourseEntity(subjectDto.getSemester().getCourse().getCourseFullName(),subjectDto.getSemester().getCourse().getCourseShortName());
                    return courseRepository.save(newCourse);
                });*/


        SemesterEntity semesterEntity =Optional.ofNullable(semesterRepository.findBySemesterId(subjectDto.getSemester().getSemesterId()))
                .orElseGet(()->{
                    SemesterEntity newSemesterEntity = new SemesterEntity(subjectDto.getSemester().getSemester(),subjectDto.getSemester().getCourse().);
                })




    }



    @Override
    public SubjectEntity getSubjectById(String subjectId) {
        return subjectRepository.findById(subjectId).orElseThrow(() -> new SubjectNotFoundException("Subject not found with given id : " + subjectId));
    }

    @Override
    public void deleteSubjectById(String subjectId) {
        subjectRepository.findById(subjectId).ifPresentOrElse(subjectRepository::delete, () -> {
            throw new SubjectNotFoundException("Subject not found with given id : " + subjectId);
        });
    }


    // wrong implementation
   /* @Override
    public SubjectEntity updateSubject(SubjectDto subjectDto, String subjectId) {

        return subjectRepository.findById(subjectId).
                map(existingSubjects -> updateExistingSubjects(existingSubjects, subjectDto))
                .map(subjectRepository::save)
                .orElseThrow(() -> new SubjectNotFoundException("Subject not found"));


    }

    private SubjectEntity updateExistingSubjects(SubjectEntity existingSubjects, SubjectDto request) {
        existingSubjects.setSubjectName(request.getSubjectName());
        existingSubjects.setSubjectCode(request.getSubjectCode());

        SemesterEntity semester = semesterRepository.findBySemester(request.getSemester().getSemester());
        existingSubjects.setSemester(semester);
        return existingSubjects;

    }*/

    @Override
    public List<SubjectEntity> getAllSubjects() {
        return subjectRepository.findAll();
    }

    @Override
    public List<SubjectEntity> getSubjectsBySubjectName(String subjectName) {
        return subjectRepository.findBySubjectName(subjectName);
    }

    @Override
    public List<SubjectEntity> getSubjectsBySubjectCodeAndSubjectName(String subjectCode, String subjectName) {
        return subjectRepository.findBySubjectNameAndSubjectCode(subjectName, subjectCode);
    }

    @Override
    public SubjectEntity getSubjectsBySubjectCode(String subjectCode) {
        return subjectRepository.findBySubjectCode(subjectCode);
    }

    @Override
    public List<SubjectEntity> getSubjectsBySubjectCodeAndSemester(String subjectCode, int sem) {
        return subjectRepository.findBySubjectCodeAndSemesterSemester(subjectCode, sem);
    }

    @Override
    public List<SubjectEntity> getSubjectsBySemesterAndCourseShortName(int sem, String courseShortName) {
        return subjectRepository.findBySemesterSemesterAndSemesterCourseCourseShortName(sem, courseShortName);
    }

    @Override
    public List<SubjectEntity> getSubjectsBySemesterAndCourseFullName(int sem, String courseFullName) {
        return subjectRepository.findBySemesterSemesterAndSemesterCourseCourseFullName(sem, courseFullName);
    }

    @Override
    public Long countSubjectsBySemesterAndCourseShortName(int sem, String courseShortName) {
        return subjectRepository.countBySemesterSemesterAndSemesterCourseCourseShortName(sem, courseShortName);
    }

}
