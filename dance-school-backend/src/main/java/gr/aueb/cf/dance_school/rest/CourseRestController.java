package gr.aueb.cf.dance_school.rest;

import gr.aueb.cf.dance_school.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.dance_school.core.exceptions.ConflictException;
import gr.aueb.cf.dance_school.core.exceptions.ValidationException;
import gr.aueb.cf.dance_school.core.filters.CourseFilters;
import gr.aueb.cf.dance_school.core.filters.Paginated;
import gr.aueb.cf.dance_school.dto.ApiResponseDTO;
import gr.aueb.cf.dance_school.dto.CourseInsertDTO;
import gr.aueb.cf.dance_school.dto.CourseReadOnlyDTO;
import gr.aueb.cf.dance_school.dto.CourseUpdateDTO;
import gr.aueb.cf.dance_school.service.CourseService;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/course")
public class CourseRestController {

    private final CourseService courseService;
    private static final Logger LOGGER = LoggerFactory.getLogger(CourseRestController.class);

    @PostMapping("/save")
    public ResponseEntity<CourseReadOnlyDTO> saveCourse(
            @Valid @RequestBody CourseInsertDTO courseInsertDTO,
            BindingResult bindingResult
    ) throws AppObjectNotFoundException, ValidationException, ConflictException {

        if(bindingResult.hasErrors()){
            throw  new ValidationException(bindingResult);
        }
        try{
            CourseReadOnlyDTO courseReadOnlyDTO = courseService.saveCourse(courseInsertDTO);
            return new ResponseEntity<>(courseReadOnlyDTO, HttpStatus.OK);
        }catch (AppObjectNotFoundException e){
            LOGGER.error("ERROR: Course can not get upload.", e);
            throw new AppObjectNotFoundException("Coach", "Ο προπονητής δεν βρέθηκε.");
        } catch (ConflictException e) {
            throw new ConflictException("Coach", "Το uuid του Προπονητή δεν μπορει να ειναι null");
        }
    }

    @PostMapping("/update")
    public ResponseEntity<CourseReadOnlyDTO> updateCourse(
            @Valid @RequestBody CourseUpdateDTO updateDTO,
            BindingResult bindingResult
            )throws AppObjectNotFoundException, ValidationException {
        if(bindingResult.hasErrors()){
            throw  new ValidationException(bindingResult);
        }
        try{
            CourseReadOnlyDTO courseReadOnlyDTO = courseService.updateCourse(updateDTO);
            return new ResponseEntity<>(courseReadOnlyDTO, HttpStatus.OK);
        }catch (AppObjectNotFoundException e){
            LOGGER.error("ERROR: Course can not update.", e);
            throw new AppObjectNotFoundException("Coach", "Ο προπονητής δεν βρέθηκε.");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponseDTO> deleteCourse(@PathVariable Long id)
            throws ConflictException, AppObjectNotFoundException {

        try {
            courseService.deleteCourse(id);
            ApiResponseDTO response = new ApiResponseDTO("Το μάθημα με Id " + id + " διαγράφηκε.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ConflictException e) {
           throw new ConflictException("Course","Υπάρχουν μαθητές εγγεγραμμένοι σε αυτό το μάθημα. Παρακαλώ αφαιρέστε όλους τους μαθητές πριν διαγράψετε το μάθημα.");
        } catch (AppObjectNotFoundException e) {
            throw new AppObjectNotFoundException("Course", "Το μάθημα δεν βρέθηκε");
        }
    }

    @PostMapping("/paginated")
    public ResponseEntity<Paginated<CourseReadOnlyDTO>> getCourseFilteredPaginated(
            @Nullable @RequestBody CourseFilters filters){

        try{
            if(filters == null) filters = CourseFilters.builder().build();
            return ResponseEntity.ok(courseService.getCourseFilteredPaginated(filters));
        }catch (Exception e){
            LOGGER.error("ERROR: Could not get teachers.", e);
            throw e;
        }
    }

    @GetMapping("/getAllCourses")
    public ResponseEntity<List<CourseReadOnlyDTO>> allCourses() {
        List<CourseReadOnlyDTO> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }




}
