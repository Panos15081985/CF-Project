package gr.aueb.cf.dance_school.mapper;

import gr.aueb.cf.dance_school.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.dance_school.dto.*;
import gr.aueb.cf.dance_school.models.*;
import gr.aueb.cf.dance_school.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Η κλάση `Mapper` παρέχει λειτουργίες μετατροπής (mapping) από και προς DTOs και οντότητες για τα αντικείμενα της εφαρμογής.
 * Η κλάση περιλαμβάνει μεθόδους που μετατρέπουν τις οντότητες `Dancer`, `Coach`, `Course`, `Guardian`, κ.ά. σε `DTOs` και αντίστροφα.
 * Επιπλέον, χρησιμοποιεί τον κωδικοποιητή `BCryptPasswordEncoder` για την κωδικοποίηση των κωδικών πρόσβασης.
 */

@Component
public class Mapper {

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public DancerReadOnlyDTO mapToDancerReadOnlyDTO(Dancer dancer){
        var dto = new DancerReadOnlyDTO();
        ContactDetailsReadOnlyDTO contactDetailsReadOnlyDTO = getContactDetailsReadOnlyDTO(dancer);

        dto.setId(dancer.getId());
        dto.setFirstname(dancer.getFirstname());
        dto.setLastname(dancer.getLastname());
        dto.setDateOfBirth(dancer.getDateOfBirth());
        dto.setUuid(dancer.getUuid());
        dto.setGender(dancer.getGenderType());
        dto.setContactDetailsReadOnlyDTO(contactDetailsReadOnlyDTO);
        dto.setContractEnd(dancer.getContractEnd());
        List<CourseReadOnlyDTO> courses = new ArrayList<>();
        for (Course course : dancer.getCourses()) {
            CourseReadOnlyDTO readOnlyDTO = mapToCourseReadOnlyDTO(course);
            courses.add(readOnlyDTO);
        }
        dto.setCourseList(courses);


        if(dancer.getGuardian() != null){
            GuardianReadOnlyDTO guardianReadOnlyDTO = new GuardianReadOnlyDTO();

            guardianReadOnlyDTO.setId(dancer.getGuardian().getId());
            guardianReadOnlyDTO.setFirstname(dancer.getGuardian().getFirstname());
            guardianReadOnlyDTO.setLastname(dancer.getGuardian().getLastname());
            dto.setGuardianReadOnlyDTO(guardianReadOnlyDTO);
        }
        return dto;
    }

    private static ContactDetailsReadOnlyDTO getContactDetailsReadOnlyDTO(Dancer dancer) {
        ContactDetailsReadOnlyDTO contactDetailsReadOnlyDTO = new ContactDetailsReadOnlyDTO();

        contactDetailsReadOnlyDTO.setId(dancer.getContactDetails().getId());
        contactDetailsReadOnlyDTO.setCity(dancer.getContactDetails().getCity());
        contactDetailsReadOnlyDTO.setPostalCode(dancer.getContactDetails().getPostalCode());
        contactDetailsReadOnlyDTO.setRoad_number(dancer.getContactDetails().getRoad_number());
        contactDetailsReadOnlyDTO.setEmail(dancer.getContactDetails().getEmail());
        contactDetailsReadOnlyDTO.setPhoneNumber(dancer.getContactDetails().getPhoneNumber());
        contactDetailsReadOnlyDTO.setRoad(dancer.getContactDetails().getRoad());
        return contactDetailsReadOnlyDTO;
    }

    public Dancer mapToDancerEntity(DancerInsertDTO dto){
        var dancer = new Dancer();
        ContactDetails contactDetails =new ContactDetails();

        contactDetails.setPostalCode(dto.getContactDetailsInsertDTO().getPostalCode());
        contactDetails.setCity(dto.getContactDetailsInsertDTO().getCity());
        contactDetails.setRoad_number(dto.getContactDetailsInsertDTO().getRoad_number());
        contactDetails.setEmail(dto.getContactDetailsInsertDTO().getEmail());
        contactDetails.setPhoneNumber(dto.getContactDetailsInsertDTO().getPhoneNumber());
        contactDetails.setRoad(dto.getContactDetailsInsertDTO().getRoad());

        dancer.setFirstname(dto.getFirstname());
        dancer.setLastname(dto.getLastname());
        dancer.setDateOfBirth(dto.getDateOfBirth());
        dancer.setContactDetails(contactDetails);
        dancer.setGenderType(dto.getGender());
        dancer.setIsActive(true);

        if(dto.getGuardianInsertDTO() != null) {
            Guardian guardian = new Guardian();
            guardian.setFirstname(dto.getGuardianInsertDTO().getFirstname());
            guardian.setLastname(dto.getGuardianInsertDTO().getLastname());
            dancer.setGuardian(guardian);
        }
        return dancer;
    }

    public CoachReadOnlyDTO mapToCoachReadOnlyDTO(Coach coach){
        ContactDetailsReadOnlyDTO contactDetailsReadOnlyDTO = getContactDetailsReadOnlyDTO(coach);

        var dto = new CoachReadOnlyDTO();
        dto.setId(coach.getId());
        dto.setFirstname(coach.getFirstname());
        dto.setLastname(coach.getLastname());
        dto.setContactDetailsReadOnlyDTO(contactDetailsReadOnlyDTO);
        dto.setRole(coach.getRole());
        dto.setUuid(coach.getUuid());
        return dto;
    }

    public ContactDetailsReadOnlyDTO getContactDetailsReadOnlyDTO(Coach coach) {
        ContactDetailsReadOnlyDTO contactDetailsReadOnlyDTO = new ContactDetailsReadOnlyDTO();

        contactDetailsReadOnlyDTO.setId(coach.getContactDetails().getId());
        contactDetailsReadOnlyDTO.setCity(coach.getContactDetails().getCity());
        contactDetailsReadOnlyDTO.setPostalCode(coach.getContactDetails().getPostalCode());
        contactDetailsReadOnlyDTO.setRoad_number(coach.getContactDetails().getRoad_number());
        contactDetailsReadOnlyDTO.setEmail(coach.getContactDetails().getEmail());
        contactDetailsReadOnlyDTO.setPhoneNumber(coach.getContactDetails().getPhoneNumber());
        contactDetailsReadOnlyDTO.setRoad(coach.getContactDetails().getRoad());

        return contactDetailsReadOnlyDTO;
    }

    public Coach mapToCoachEntity(CoachInsertDTO dto){
        Coach coach = new Coach();
        ContactDetails contactDetails = new ContactDetails();

        contactDetails.setCity(dto.getContactDetailsInsertDTO().getCity());
        contactDetails.setRoad_number(dto.getContactDetailsInsertDTO().getRoad_number());
        contactDetails.setPostalCode(dto.getContactDetailsInsertDTO().getPostalCode());
        contactDetails.setEmail(dto.getContactDetailsInsertDTO().getEmail());
        contactDetails.setPhoneNumber(dto.getContactDetailsInsertDTO().getPhoneNumber());
        contactDetails.setRoad(dto.getContactDetailsInsertDTO().getRoad());

        coach.setFirstname(dto.getFirstname());
        coach.setLastname(dto.getLastname());
        coach.setRole(dto.getRole());
        coach.setUsername(dto.getUsername());
        coach.setContactDetails(contactDetails);
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        coach.setPassword(encodedPassword);
        return coach;
    }

    public ContactDetails mapToContactDetailsEntity(ContactDetailsInsertDTO contactDetailsInsertDTO){
        ContactDetails contactDetails = new ContactDetails();

        contactDetails.setCity(contactDetailsInsertDTO.getCity());
        contactDetails.setRoad_number(contactDetailsInsertDTO.getRoad_number());
        contactDetails.setPostalCode(contactDetailsInsertDTO.getPostalCode());
        contactDetails.setEmail(contactDetailsInsertDTO.getEmail());
        contactDetails.setPhoneNumber(contactDetailsInsertDTO.getPhoneNumber());
        contactDetails.setRoad(contactDetailsInsertDTO.getRoad());

        return contactDetails;
    }

    public Course mapToCourseEntity(CourseInsertDTO dto){
        Course course = new Course();
        course.setName(dto.getName());
        course.setDay(dto.getDay());
        course.setStartTime(dto.getStartTime());
        course.setEndTime(dto.getEndTime());
        course.setDescription(dto.getDescription());
        return course;

    }

    public CourseReadOnlyDTO mapToCourseReadOnlyDTO(Course course){
        CourseReadOnlyDTO dto = new CourseReadOnlyDTO();

        dto.setId(course.getId());
        dto.setName(course.getName());
        dto.setDay(course.getDay());
        dto.setStartTime(course.getStartTime());
        dto.setEndTime(course.getEndTime());
        dto.setDescription(course.getDescription());
        String lastnameFirstname = course.getCoach().getLastname() + " " + course.getCoach().getFirstname();
        dto.setFirstnameLastnameCoach(lastnameFirstname);
        dto.setRole(course.getCoach().getRole());
        dto.setCoachUuid(course.getCoach().getUuid());
        return dto;
    }

    public Guardian mapToGuardianEntity(GuardianInsertDTO dto){
        Guardian guardian =new Guardian();

        guardian.setFirstname(dto.getFirstname());
        guardian.setLastname(dto.getLastname());
        return guardian;
    }
}
