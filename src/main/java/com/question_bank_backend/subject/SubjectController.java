package com.question_bank_backend.subject;


import com.question_bank_backend.exceptions.CourseNotFoundException;
import com.question_bank_backend.exceptions.SubjectNotFoundException;
import com.question_bank_backend.utility.MyResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "${api.prefix}/subjects")
public class SubjectController {


    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }


    @GetMapping("/subject/by/subjectCode-Semester")
    public ResponseEntity<Object> getSubjectsBySubjectCodeAndSemester(@RequestParam String subjectCode, @RequestParam int semester) {
        try {
            List<SubjectEntity> subjectEntityList = subjectService.getSubjectsBySubjectCodeAndSemester(subjectCode, semester);
            return MyResponseHandler.generateResponse(HttpStatus.OK, false, "found!", subjectEntityList);
        } catch (SubjectNotFoundException e) {
            return MyResponseHandler.generateResponse(HttpStatus.NOT_FOUND, true, e.getMessage(), null);
        }
    }

    @GetMapping("/subject/by/semester-courseShortName")
    public ResponseEntity<Object> getSubjectsBySemesterAndCourseShortName(@RequestParam int sem, @RequestParam String courseShortName) {
        try {
            List<SubjectEntity> subjectEntityList = subjectService.getSubjectsBySemesterAndCourseShortName(sem, courseShortName);
            return MyResponseHandler.generateResponse(HttpStatus.OK, false, "found!", subjectEntityList);
        } catch (CourseNotFoundException e) {
            return MyResponseHandler.generateResponse(HttpStatus.NOT_FOUND, true, e.getMessage(), null);
        }
    }


    @GetMapping("/subject/by/semester-courseFullName")
    public ResponseEntity<Object> getSubjectsBySemesterAndCourseFullName(@RequestParam int sem, @RequestParam String courseFullName) {
        try {
            List<SubjectEntity> subjectEntityList = subjectService.getSubjectsBySemesterAndCourseFullName(sem, courseFullName);
            return MyResponseHandler.generateResponse(HttpStatus.OK, false, "found!", subjectEntityList);
        } catch (CourseNotFoundException e) {
            return MyResponseHandler.generateResponse(HttpStatus.NOT_FOUND, true, e.getMessage(), null);
        }
    }

    @GetMapping("/subject/count/by/semester-courseShortName")
    public ResponseEntity<Object> countSubjectsBySemesterAndCourseShortName(@RequestParam int sem, @RequestParam String courseShortName) {
        try {
            Long count = subjectService.countSubjectsBySemesterAndCourseShortName(sem, courseShortName);
            return MyResponseHandler.generateResponse(HttpStatus.OK, false, "found!", count);
        } catch (CourseNotFoundException e) {
            return MyResponseHandler.generateResponse(HttpStatus.NOT_FOUND, true, e.getMessage(), null);
        }
    }

    @GetMapping("/subject/by/subject-code")
    public ResponseEntity<Object> getSubjectsBySubjectCode(@RequestParam String subjectCode) {
        try {
            SubjectEntity subjectEntity = subjectService.getSubjectsBySubjectCode(subjectCode);
            return MyResponseHandler.generateResponse(HttpStatus.OK, false, "found!", subjectEntity);
        } catch (SubjectNotFoundException e) {
            return MyResponseHandler.generateResponse(HttpStatus.NOT_FOUND, true, e.getMessage(), null);
        }
    }

    @GetMapping("/subject/by/subjectCode-subjectName")
    public ResponseEntity<Object> getSubjectsBySubjectCodeAndSubjectName(@RequestParam String subjectCode, @RequestParam String subjectName) {
        try {
            List<SubjectEntity> subjectEntityList = subjectService.getSubjectsBySubjectCodeAndSubjectName(subjectCode, subjectName);
            return MyResponseHandler.generateResponse(HttpStatus.OK, false, "found!", subjectEntityList);
        } catch (SubjectNotFoundException e) {
            return MyResponseHandler.generateResponse(HttpStatus.NOT_FOUND, true, e.getMessage(), null);
        }
    }

    @GetMapping("/subject/by/subjectName")
    public ResponseEntity<Object> getSubjectsBySubjectName(@RequestParam String subjectName) {
        try {
            List<SubjectEntity> subjectEntityList = subjectService.getSubjectsBySubjectName(subjectName);
            return MyResponseHandler.generateResponse(HttpStatus.OK, false, "found!", subjectEntityList);
        } catch (SubjectNotFoundException e) {
            return MyResponseHandler.generateResponse(HttpStatus.NOT_FOUND, true, e.getMessage(), null);
        }
    }

    @GetMapping("/subject/all")
    public ResponseEntity<Object> getAllSubjects() {
        try {
            List<SubjectEntity> subjectEntityList = subjectService.getAllSubjects();
            return MyResponseHandler.generateResponse(HttpStatus.OK, false, "found!", subjectEntityList);
        } catch (Exception e) {
            return MyResponseHandler.generateResponse(HttpStatus.NOT_FOUND, true, "Error!", null);
        }
    }

    @DeleteMapping("/subject/{id}/delete")
    public ResponseEntity<Object> deleteSubjectBySubjectId(@PathVariable String id) {
        try {
            subjectService.deleteSubjectById(id);
            return MyResponseHandler.generateResponse(HttpStatus.OK, false, "deleted!", null);
        } catch (SubjectNotFoundException e) {
            return MyResponseHandler.generateResponse(HttpStatus.NOT_FOUND, true, e.getMessage(), null);
        }
    }

    @GetMapping("/subject/{id}/subject")
    public ResponseEntity<Object> getSubjectById(@PathVariable String id) {
        try {
            SubjectEntity subjectEntity = subjectService.getSubjectById(id);
            return MyResponseHandler.generateResponse(HttpStatus.OK, false, "found!", subjectEntity);
        } catch (SubjectNotFoundException e) {
            return MyResponseHandler.generateResponse(HttpStatus.NOT_FOUND, true, e.getMessage(), null);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addSubject(@RequestBody SubjectDto subjectDto) {
        try {
            if (subjectDto != null) {
                SubjectEntity subjectEntity = subjectService.addSubject(subjectDto);
                return MyResponseHandler.generateResponse(HttpStatus.OK, false, "added!", subjectEntity);
            }
        } catch (Exception e) {
            return MyResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, true, "Error!", null);
        }

        return MyResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, true, "Error!", null);
    }


    @PutMapping("/subject/{id}/update")
    public ResponseEntity<Object> updateSubject(@PathVariable String id, @RequestBody SubjectDto subjectDto) {
        try {
            if (subjectDto != null) {
                SubjectEntity subjectEntity = subjectService.updateSubject(subjectDto, id);
                return MyResponseHandler.generateResponse(HttpStatus.OK, false, "updated!", subjectEntity);
            }
        } catch (SubjectNotFoundException e) {
            return MyResponseHandler.generateResponse(HttpStatus.NOT_FOUND, true, e.getMessage(), null);
        }
        return MyResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, true, "Error!", null);
    }


}
