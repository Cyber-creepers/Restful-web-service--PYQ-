package com.question_bank_backend.question;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.question_bank_backend.exceptions.QuestionNotFoundException;
import com.question_bank_backend.exceptions.SubjectNotFoundException;
import com.question_bank_backend.utility.MyResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.FileAlreadyExistsException;
import java.util.List;

@RestController
@RequestMapping(path = "${api.prefix}/questions")
public class QuestionController {

    private final QuestionService questionService;

    private final ObjectMapper objectMapper;


    QuestionController(QuestionService questionService, ObjectMapper objectMapper) {
        this.questionService = questionService;
        this.objectMapper = objectMapper;
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

    @GetMapping("/question/by/subjectCode-year")
    public ResponseEntity<Object> getQuestionsBySubjectCodeAndYear(@RequestParam String subjectCode, @RequestParam int year) {
        try {
            List<QuestionEntity> questionEntities = questionService.getQuestionBySubjectCodeAndYear(subjectCode, year);
            return MyResponseHandler.generateResponse(HttpStatus.OK, false, "found!", questionEntities);
        } catch (QuestionNotFoundException e) {
            return MyResponseHandler.generateResponse(HttpStatus.NOT_FOUND, true, e.getMessage(), null);
        }
    }

    @GetMapping("/question/by/subjectCode")
    public ResponseEntity<Object> getQuestionsBySubjectCode(@RequestParam String subjectCode) {
        try {
            List<QuestionEntity> questionentities = questionService.getQuestionBySubjectCode(subjectCode);
            return MyResponseHandler.generateResponse(HttpStatus.OK, false, "found!", questionentities);
        } catch (QuestionNotFoundException e) {
            return MyResponseHandler.generateResponse(HttpStatus.NOT_FOUND, true, e.getMessage(), null);
        }
    }

    @GetMapping("/question/{year}/all")
    public ResponseEntity<Object> getQuestionsByYear(@PathVariable int year) {
        try {
            List<QuestionEntity> questionEntities = questionService.getQuestionByYear(year);
            return MyResponseHandler.generateResponse(HttpStatus.OK, false, "found!", questionEntities);
        } catch (QuestionNotFoundException e) {
            return MyResponseHandler.generateResponse(HttpStatus.NOT_FOUND, true, e.getMessage(), null);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllQuestions() {
        try {
            List<QuestionEntity> questionEntities = questionService.getAllQuestions();
            return MyResponseHandler.generateResponse(HttpStatus.OK, false, "found!", questionEntities);
        } catch (QuestionNotFoundException e) {
            return MyResponseHandler.generateResponse(HttpStatus.NOT_FOUND, true, e.getMessage(), null);
        }
    }

    @GetMapping("/question/{id}/question")
    public ResponseEntity<Object> getQuestionByQuestionId(@PathVariable String id) {
        try {
            QuestionEntity question = questionService.getQuestionById(id);
            return MyResponseHandler.generateResponse(HttpStatus.OK, false, "found!", question);
        } catch (QuestionNotFoundException e) {
            return MyResponseHandler.generateResponse(HttpStatus.NOT_FOUND, true, e.getMessage(), null);
        }
    }

    @DeleteMapping("/question/{id}/delete")
    public ResponseEntity<Object> deleteQuestionByQuestionId(@PathVariable String id) {
        try {
            questionService.deleteQuestionById(id);
            return MyResponseHandler.generateResponse(HttpStatus.OK, false, "Deleted!", null);
        } catch (QuestionNotFoundException e) {
            return MyResponseHandler.generateResponse(HttpStatus.NOT_FOUND, true, e.getMessage(), null);
        }
    }

    @PutMapping("/question/{id}/uodate")
    public ResponseEntity<Object> updateQuestion(@PathVariable String id, @RequestBody QuestionDto questionDto) {
        try {

            QuestionEntity question = questionService.updateQuestion(questionDto, id);
            return MyResponseHandler.generateResponse(HttpStatus.OK, false, "Updated!", question);
        } catch (QuestionNotFoundException e) {
            return MyResponseHandler.generateResponse(HttpStatus.NOT_FOUND, true, e.getMessage(), null);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addQuestion(@RequestPart MultipartFile file, @RequestPart String request) {
        try {
            QuestionEntity question = questionService.addQuestion(file, convertRequest(request));
            return MyResponseHandler.generateResponse(HttpStatus.OK, false, "Added successfully ", question);
        } catch (FileAlreadyExistsException | JsonProcessingException e) {
            return MyResponseHandler.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR, true, e.getMessage(), null);
        }
    }

    private AddQuestionRequest convertRequest(String request) throws JsonProcessingException {
        return objectMapper.readValue(request, AddQuestionRequest.class);
    }


}
