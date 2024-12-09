package com.question_bank_backend.semester;


import com.question_bank_backend.exceptions.CourseNotFoundException;
import com.question_bank_backend.exceptions.SemesterNotFoundException;
import com.question_bank_backend.utility.MyResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "${api.prefix}/semesters}")
public class SemesterController {

    private final SemesterService semesterService;

    public SemesterController(SemesterService semesterService) {
        this.semesterService = semesterService;
    }


    @GetMapping("/semester/by/course-full-name")
    public ResponseEntity<Object> getAllSemestersByCourseFullName(@RequestParam String courseFullName) {
        try {
            List<SemesterEntity> semesterEntities = semesterService.getAllSemestersByCourseFullName(courseFullName);
            return MyResponseHandler.generateResponse(HttpStatus.OK, false, "Found!", semesterEntities);
        } catch (CourseNotFoundException e) {
            return MyResponseHandler.generateResponse(HttpStatus.NOT_FOUND, true, e.getMessage(), null);
        }
    }


    @GetMapping("/semester/by/course-short-name")
    public ResponseEntity<Object> getAllSemesterByCourseShortName(@RequestParam String courseShortName) {
        try {
            List<SemesterEntity> semesterEntities = semesterService.getAllSemestersByCourseShortName(courseShortName);
            return MyResponseHandler.generateResponse(HttpStatus.OK, false, "found!", semesterEntities);
        } catch (CourseNotFoundException e) {
            return MyResponseHandler.generateResponse(HttpStatus.NOT_FOUND, true, e.getMessage(), null);
        }
    }

    @GetMapping("/semester/{courseId}/all")
    public ResponseEntity<Object> getAllSemesterByCourseId(@PathVariable String courseId) {
        try {
            List<SemesterEntity> semesterEntities = semesterService.getAllSemestersByCourseId(courseId);
            return MyResponseHandler.generateResponse(HttpStatus.OK, false, "found!", semesterEntities);
        } catch (CourseNotFoundException e) {
            return MyResponseHandler.generateResponse(HttpStatus.NOT_FOUND, true, e.getMessage(), null);
        }
    }

    @PutMapping("/semester/{id}/update")
    public ResponseEntity<Object> updateSemester(@PathVariable String id, @RequestBody SemesterDto semesterDto) {
        try {
            SemesterEntity semesterEntity = semesterService.updateSemesterBySemesterId(semesterDto, id);
            return MyResponseHandler.generateResponse(HttpStatus.OK, false, "Updated!", semesterEntity);
        } catch (SemesterNotFoundException e) {
            return MyResponseHandler.generateResponse(HttpStatus.NOT_FOUND, true, e.getMessage(), null);
        }
    }

    @GetMapping("/semester/{id}/semester")
    public ResponseEntity<Object> getSemesterById(@PathVariable String id) {
        try {
            SemesterEntity semesterEntity = semesterService.getSemesterBySemesterId(id);
            return MyResponseHandler.generateResponse(HttpStatus.OK, false, "found!", semesterEntity);
        } catch (SemesterNotFoundException e) {
            return MyResponseHandler.generateResponse(HttpStatus.NOT_FOUND, true, e.getMessage(), null);
        }
    }

    @DeleteMapping("/semester/{id}/delete")
    public ResponseEntity<Object> deleteSemesterSemesterId(@PathVariable String id) {
        try {
            semesterService.deleteSemesterBySemesterId(id);
            return MyResponseHandler.generateResponse(HttpStatus.OK, false, "Deleted!", null);
        } catch (SemesterNotFoundException e) {
            return MyResponseHandler.generateResponse(HttpStatus.NOT_FOUND, true, e.getMessage(), null);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addSemester(@RequestBody SemesterDto semesterDto) {
        try{
            if (semesterDto!=null){
                SemesterEntity semesterEntity = semesterService.addSemester(semesterDto);
                return MyResponseHandler.generateResponse(HttpStatus.OK, false, "Added!", semesterEntity);
            }
        }catch (Exception e){
            return MyResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, true, "Error!", null);
        }
        return MyResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, true, "Error!", null);
    }

}
