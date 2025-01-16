package gr.aueb.cf.dance_school.service;

import gr.aueb.cf.dance_school.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.dance_school.core.exceptions.ConflictException;
import gr.aueb.cf.dance_school.core.filters.CourseFilters;
import gr.aueb.cf.dance_school.core.filters.Paginated;
import gr.aueb.cf.dance_school.core.specification.CourseSpecification;
import gr.aueb.cf.dance_school.dto.CourseInsertDTO;
import gr.aueb.cf.dance_school.dto.CourseReadOnlyDTO;
import gr.aueb.cf.dance_school.dto.CourseUpdateDTO;
import gr.aueb.cf.dance_school.mapper.Mapper;
import gr.aueb.cf.dance_school.models.Coach;
import gr.aueb.cf.dance_school.models.Course;
import gr.aueb.cf.dance_school.repository.CoachRepository;
import gr.aueb.cf.dance_school.repository.CourseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CoachRepository coachRepository;
    private final Mapper mapper;

    @Transactional(rollbackOn = Exception.class)
    public CourseReadOnlyDTO saveCourse(CourseInsertDTO courseInsertDTO)
            throws AppObjectNotFoundException, ConflictException {

        if(courseInsertDTO.getCoachUuid() == null) {
            throw new ConflictException("Coach", "Το uuid του Προπονητή δεν μπορει να ειναι null");
        }
            Coach coach = coachRepository.findByUuid(courseInsertDTO.getCoachUuid())
                    .orElseThrow(() -> new AppObjectNotFoundException("Coach", "Ο προπονητής δεν βρέθηκε" + courseInsertDTO.getCoachUuid()));

            Course course = mapper.mapToCourseEntity(courseInsertDTO);
            course.setCoach(coach);

        try {
            Course savedCourse = courseRepository.save(course);
            return mapper.mapToCourseReadOnlyDTO(savedCourse);
        } catch (Exception e) {
            throw  e;
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public void deleteCourse(Long id) throws AppObjectNotFoundException, ConflictException {
        System.out.println(id);
        Course course = courseRepository.findCourseById(id).orElseThrow(()
                ->  new AppObjectNotFoundException("Course", "Το μάθημα δεν βρέθηκε"));

        // Ελέγχουμε αν υπάρχουν εγγεγραμμένοι μαθητές στο μάθημα.
        // Εάν υπάρχουν, ρίχνουμε εξαίρεση για να αποτρέψουμε τη διαγραφή του μαθήματος.
        if (course.getDancers() != null && !course.getDancers().isEmpty()) {
            System.out.println("edw");
            throw new ConflictException("Course","Υπάρχουν μαθητές εγγεγραμμένοι σε αυτό το μάθημα. Παρακαλώ αφαιρέστε όλους τους μαθητές πριν διαγράψετε το μάθημα.");
        }

        // Ελέγχουμε αν υπάρχει προπονητής για το μάθημα.
        // Αν υπάρχει, αφαιρούμε το μάθημα από τη λίστα των μαθημάτων του προπονητή και αποθηκεύουμε τις αλλαγές.
        if(course.getCoach() != null){
            course.getCoach().getCourses().remove(course);
           coachRepository.save(course.getCoach());
        }

        courseRepository.delete(course);
    }

    @Transactional(rollbackOn = Exception.class)
    public CourseReadOnlyDTO updateCourse(CourseUpdateDTO updateDTO)
        throws AppObjectNotFoundException{

        Course course = courseRepository.findCourseById(updateDTO.getId()).orElseThrow(()
            -> new AppObjectNotFoundException("Course", "Το μάθημα δεν βρέθηκε"));

        // Ενημερώνουμε τα πεδία του Course entity με τα δεδομένα του CourseUpdateDTO
        Coach coach = coachRepository.findByUuid(updateDTO.getCoachUuid())
                        .orElseThrow(()-> new AppObjectNotFoundException("Course", "Το μάθημα δεν βρέθηκε"));
        course.setName(updateDTO.getName());
        course.setDescription(updateDTO.getDescription());
        course.setDay(updateDTO.getDay());
        course.setStartTime(updateDTO.getStartTime());
        course.setEndTime(updateDTO.getEndTime());
        course.setCoach(coach);

        // Ενημερώνουμε το Course στη βάση δεδομένων
        Course updatedCourse = courseRepository.save(course);  // Αντικαθιστούμε την προηγούμενη έκδοση του Course με την ενημερωμένη

        // Επιστρέφουμε το CourseReadOnlyDTO για το ενημερωμένο μάθημα
        return mapper.mapToCourseReadOnlyDTO(updatedCourse);
    }

    @Transactional
    public Paginated<CourseReadOnlyDTO> getCourseFilteredPaginated(CourseFilters filters){
        var filtered = courseRepository.findAll(getSpecsFromFilters(filters),filters.getPageable());
        return new Paginated<>(filtered.map(mapper::mapToCourseReadOnlyDTO));
    }


    private Specification<Course> getSpecsFromFilters(CourseFilters filters){
        return Specification
                .where(CourseSpecification.nameLike(filters.getName()))
                .and(CourseSpecification.dayLike(filters.getDay()))
                .and(CourseSpecification.byCoachId(filters.getCoachId()));

    }

    @Transactional
    public List<CourseReadOnlyDTO> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream()
                .map(course ->{
                    CourseReadOnlyDTO dto = mapper.mapToCourseReadOnlyDTO(course);
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
