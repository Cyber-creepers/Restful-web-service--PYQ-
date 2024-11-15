package com.question_bank_backend.question;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.question_bank_backend.course.CourseRepository;
import com.question_bank_backend.exceptions.QuestionNotFoundException;
import com.question_bank_backend.semester.SemesterRepository;
import com.question_bank_backend.subject.SubjectEntity;
import com.question_bank_backend.subject.SubjectRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionServiceImp implements QuestionService {

    private final QuestionRepository questionRepository;
    private final ObjectMapper objectMapper;
    private final SubjectRepository subjectRepository;
    private final SemesterRepository semesterRepository;
    private final CourseRepository courserRepository;

    @Value("${project.questions}")
    private String path;

    public QuestionServiceImp(QuestionRepository questionRepository, ObjectMapper objectMapper, SubjectRepository subjectRepository, SemesterRepository semesterRepository, CourseRepository courserRepository) {
        this.questionRepository = questionRepository;
        this.objectMapper = objectMapper;
        this.subjectRepository = subjectRepository;
        this.semesterRepository = semesterRepository;
        this.courserRepository = courserRepository;
    }

    @Override
    public QuestionEntity addQuestion(MultipartFile files,AddQuestionRequest question) throws FileAlreadyExistsException {

        // check if the subject is found in the db
        // if yes , set it as the new question subject
        // if no , then save it as a new subject
        // then set as the new question subject.

        if (Files.exists(Paths.get(path + File.separator + files.getOriginalFilename()))){
            throw new FileAlreadyExistsException("File already exists! Please enter file name! ");
        }



        SubjectEntity subject= Optional.ofNullable(subjectRepository.findBySubjectCode(question.getSubject().getSubjectCode()))
                .orElseGet(()-> {
                    SubjectEntity newSubject = new SubjectEntity(question.getSubject().getSubjectName(), question.getSubject().getSubjectCode());
                    return subjectRepository.save(newSubject);
                });

        question.setSubject(subject);


        return null;
    }

    private QuestionEntity createQuestion(AddQuestionRequest questionRequest, SubjectEntity subject){
        return new QuestionEntity(
                questionRequest.getSubject(),
                questionRequest.getQuestion(),
                questionRequest.getYear() )
    }





    @Override
    public QuestionEntity updateQuestion(QuestionEntity question, String id) {
        return null;
    }




    @Override
    public QuestionEntity getQuestionById(String id) {
        return questionRepository.findById(id).orElseThrow(() -> new QuestionNotFoundException("Question not found! "));
    }

    @Override
    public void deleteQuestionById(String id) {
        questionRepository.findById(id).ifPresentOrElse(questionRepository::delete, () -> {
            throw new QuestionNotFoundException("Question not found!");
        });
    }

    @Override
    public List<QuestionEntity> getAllQuestions() {
        return questionRepository.findAll();
    }

    @Override
    public List<QuestionEntity> getQuestionByYear(int year) {
        return questionRepository.findByYear(year);
    }

    @Override
    public List<QuestionEntity> getQuestionBySubjectCode(String subjectCode) {
        return questionRepository.findBySubjectSubjectCode(subjectCode);
    }

    @Override
    public List<QuestionEntity> getQuestionBySubjectCodeAndYear(String subjectCode, int year) {
        return questionRepository.findBySubjectSubjectCodeAndYear(subjectCode, year);
    }

    @Override
    public List<QuestionEntity> getQuestionBySubjectNameAndYear(String subjectName, int year) {
        return questionRepository.findBySubjectSubjectNameAndYear(subjectName, year);
    }

    @Override
    public List<QuestionEntity> getQuestionBySemesterAndSubjectName(int semester, String subjectName) {
        return questionRepository.findBySubject_Semester_SemAndSubject_SubjectName(semester, subjectName);
    }

    @Override
    public List<QuestionEntity> getQuestionBySemesterAndSubjectNameAndYear(int semester, String subjectName, int year) {
        return questionRepository.findBySubject_Semester_SemAndSubject_SubjectNameAndYear(semester, subjectName, year);
    }

    @Override
    public List<QuestionEntity> getQuestionBySemesterAndSubjectCode(int semester, String subjectCode) {
        return questionRepository.findBySubject_Semester_SemAndSubject_SubjectCode(semester, subjectCode);
    }

    @Override
    public List<QuestionEntity> getQuestionByCourseFullName(String courseFullName) {
        return questionRepository.findBySubject_Semester_Course_CourseFullName(courseFullName);
    }

    @Override
    public List<QuestionEntity> getQuestionByCourseShortName(String courseShortName) {
        return questionRepository.findBySubject_Semester_Course_CourseShortName(courseShortName);
    }
}
