package com.question_bank_backend.question;


import com.question_bank_backend.exceptions.QuestionNotFoundException;
import com.question_bank_backend.exceptions.SubjectNotFoundException;
import com.question_bank_backend.utility.MyResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "${api.prefix}/questions")
public class QuestionController {

    private final QuestionService questionService;

    QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }


    @GetMapping("/question/by/course-short-name")
    public ResponseEntity<Object> getQuestionsByCourseShortName(@RequestParam String courseShortName) {
        try {
            List<QuestionEntity> questionEntityList = questionService.getQuestionByCourseShortName(courseShortName);
            return MyResponseHandler.generateResponse(HttpStatus.OK, false, "found!", questionEntityList);
        } catch (QuestionNotFoundException e) {
            return MyResponseHandler.generateResponse(HttpStatus.NOT_FOUND, true, e.getMessage(), null);
        }
    }

    @GetMapping("/question/by/course-full-name")
    public ResponseEntity<Object> getQuestionByCourseFullName(@RequestParam String courseFullName) {
        try {
            List<QuestionEntity> questionEntityList = questionService.getQuestionByCourseFullName(courseFullName);
            return MyResponseHandler.generateResponse(HttpStatus.OK, false, "found!", questionEntityList);
        } catch (QuestionNotFoundException e) {
            return MyResponseHandler.generateResponse(HttpStatus.NOT_FOUND, true, e.getMessage(), null);
        }
    }

    @GetMapping("/question/by/semester-subjectCode")
    public ResponseEntity<Object> getQuestionBySemesterAndSubjectCode(@RequestParam int sem, @RequestParam String subjectCode) {
        try {
            List<QuestionEntity> questionEntities = questionService.getQuestionBySemesterAndSubjectCode(sem, subjectCode);
            return MyResponseHandler.generateResponse(HttpStatus.OK, false, "found!", questionEntities);
        } catch (SubjectNotFoundException e) {
            return MyResponseHandler.generateResponse(HttpStatus.NOT_FOUND, true, e.getMessage(), null);
        }
    }

    @GetMapping("/question/by/semester-subjectName-year")
    public ResponseEntity<Object> getQuestionBySemesterAndSubjectNameAndYear(@RequestParam int sem, @RequestParam String subjectName, @RequestParam int year) {
        try {
            List<QuestionEntity> questionEntities = questionService.getQuestionBySemesterAndSubjectNameAndYear(sem, subjectName, year);
            return MyResponseHandler.generateResponse(HttpStatus.OK, false, "found!", questionEntities);
        } catch (QuestionNotFoundException e) {
            return MyResponseHandler.generateResponse(HttpStatus.NOT_FOUND, true, e.getMessage(), null);
        }
    }

    @GetMapping("/question/by/semester-subjectName")
    public ResponseEntity<Object> getQuestionsBySemesterAndSubjectName(@RequestParam int sem, @RequestParam String subjectName) {
        try {
            List<QuestionEntity> questionEntities = questionService.getQuestionBySemesterAndSubjectName(sem, subjectName);
            return MyResponseHandler.generateResponse(HttpStatus.OK, false, "found!", questionEntities);
        } catch (QuestionNotFoundException e) {
            return MyResponseHandler.generateResponse(HttpStatus.NOT_FOUND, true, e.getMessage(), null);
        }
    }

    @GetMapping("/question/by/subjectName-year")
    public ResponseEntity<Object> getQuestionBySubjectNameAndYear(@RequestParam String subjectName, @RequestParam int year) {
        try {
            List<QuestionEntity> questionEntities = questionService.getQuestionBySubjectNameAndYear(subjectName, year);
            return MyResponseHandler.generateResponse(HttpStatus.OK, false, "found!", questionEntities);
        } catch (QuestionNotFoundException e) {
            return MyResponseHandler.generateResponse(HttpStatus.NOT_FOUND, true, e.getMessage(), null);
        }
    }


}
