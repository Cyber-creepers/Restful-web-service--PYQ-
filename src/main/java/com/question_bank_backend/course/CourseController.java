package com.question_bank_backend.course;


import com.question_bank_backend.exceptions.AlreadyExistsException;
import com.question_bank_backend.exceptions.ResourceNotFoundException;
import com.question_bank_backend.utility.MyResponseHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(path = "${api.prefix}/courses")
public class CourseController {

    private final CourseService courseService;

    CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllCourses() {
        try {
            List<CourseEntity> courses = courseService.getAllCourses();
            return MyResponseHandler.generateResponse(OK, false, "Found!", courses);
        } catch (Exception e) {
            return MyResponseHandler.generateResponse(INTERNAL_SERVER_ERROR, true, "Error", null);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addCourse(@RequestBody CourseDto courseDto){
        try{
            CourseEntity courseEntity = courseService.addCourse(courseDto);
            return MyResponseHandler.generateResponse(OK, false, "Added Course", courseEntity);
        }catch (AlreadyExistsException e ){
            return MyResponseHandler.generateResponse(CONFLICT,true, e.getMessage(),null);
        }
    }

    @GetMapping("/course/{id}/course")
    public ResponseEntity<Object> getCourseById(@PathVariable String id){
        try{
            CourseEntity courseEntity = courseService.getCourseByCoursedId(id);
            return MyResponseHandler.generateResponse(OK, false, "Found!", courseEntity);
        }catch (ResourceNotFoundException e){
            return MyResponseHandler.generateResponse(NOT_FOUND, true, e.getMessage(),null);
        }
    }

    @DeleteMapping("/course/{id}/delete")
    public ResponseEntity<Object> deleteCategory(@PathVariable String id){
        try{
            courseService.deleteCourseByCourseId(id);
            return MyResponseHandler.generateResponse(OK, false, "Deleted Successfully", null);
        }catch (ResourceNotFoundException e){
            return MyResponseHandler.generateResponse(NOT_FOUND, true, e.getMessage(),null);
        }
    }

    @PutMapping("/course/{id}/update")
    public ResponseEntity<Object> updateCourse(@PathVariable String id, @RequestBody CourseDto courseDto){
        try{
            CourseEntity courseEntity = courseService.updateCourseByCourseId(courseDto, id);
            return MyResponseHandler.generateResponse(OK, false, "Updated Successfully", courseEntity);
        }catch (ResourceNotFoundException e){
            return MyResponseHandler.generateResponse(NOT_FOUND, true, e.getMessage(), null);
        }
    }

}


