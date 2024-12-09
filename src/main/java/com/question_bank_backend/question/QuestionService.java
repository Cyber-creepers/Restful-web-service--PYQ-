package com.question_bank_backend.question;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.FileAlreadyExistsException;
import java.util.List;

public interface QuestionService {

    QuestionEntity addQuestion(MultipartFile file, AddQuestionRequest question) throws FileAlreadyExistsException;

    QuestionEntity updateQuestion(QuestionEntity question, String id);

    QuestionEntity getQuestionById(String id);

    void deleteQuestionById(String id);

    List<QuestionEntity> getAllQuestions();

    List<QuestionEntity> getQuestionByYear(int year);

    List<QuestionEntity> getQuestionBySubjectCode(String subjectCode);

    List<QuestionEntity> getQuestionBySubjectCodeAndYear(String subjectCode, int year);

    List<QuestionEntity> getQuestionBySubjectNameAndYear(String subjectName, int year);

    List<QuestionEntity> getQuestionBySemesterAndSubjectName(int semester, String subjectName);

    List<QuestionEntity> getQuestionBySemesterAndSubjectNameAndYear(int semester, String subjectName, int year);

    List<QuestionEntity> getQuestionBySemesterAndSubjectCode(int semester, String subjectCode);

    List<QuestionEntity> getQuestionByCourseFullName(String courseFullName);

    List<QuestionEntity> getQuestionByCourseShortName(String courseShortName);
}
