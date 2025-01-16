package gr.aueb.cf.dance_school.service;

import gr.aueb.cf.dance_school.core.enums.RoleType;
import gr.aueb.cf.dance_school.core.exceptions.AppObjectAlreadyExistsException;
import gr.aueb.cf.dance_school.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.dance_school.core.filters.CoachFilters;
import gr.aueb.cf.dance_school.core.filters.Paginated;
import gr.aueb.cf.dance_school.core.specification.CoachSpecification;
import gr.aueb.cf.dance_school.dto.*;
import gr.aueb.cf.dance_school.mapper.Mapper;
import gr.aueb.cf.dance_school.models.Coach;
import gr.aueb.cf.dance_school.models.ContactDetails;
import gr.aueb.cf.dance_school.models.Course;
import gr.aueb.cf.dance_school.repository.CoachRepository;
import gr.aueb.cf.dance_school.repository.ContactDetailsRepository;
import gr.aueb.cf.dance_school.repository.CourseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CoachService {

   private final CoachRepository coachRepository;
   private final CourseRepository courseRepository;
   private final ContactDetailsRepository contactDetailsRepository;
   private final Mapper mapper;
   BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

   @Transactional(rollbackOn = Exception.class)
   public CoachReadOnlyDTO saveCoach(CoachInsertDTO dto) throws AppObjectAlreadyExistsException{

       if(coachRepository.findByUsername(dto.getUsername()).isPresent()){
           throw new AppObjectAlreadyExistsException("Coach", "Ο προπονητής υπάρχει ήδη με όνομα χρήστη: " + dto.getUsername());
       }
       Coach coach = mapper.mapToCoachEntity(dto);
       Coach savedCoach = coachRepository.save(coach);
       return mapper.mapToCoachReadOnlyDTO(savedCoach);
   }

    /**
     * Διαγράφει έναν προπονητή από τη βάση δεδομένων.
     * Ελέγχει αν ο προπονητής έχει ανατεθεί σε κάποιο μάθημα. Αν ναι, αναθέτει τα μαθήματα σε έναν προπονητή με ρόλο "Admin".
     * Αν δεν βρεθεί ο προπονητής ή ο προπονητής Admin, εκτοξεύεται εξαίρεση `AppObjectNotFoundException`.
     * Στη συνέχεια, διαγράφει τον προπονητή από τη βάση δεδομένων.
     */
    @Transactional(rollbackOn = Exception.class)
    public void deleteCoach(String uuid) throws AppObjectNotFoundException{
       Coach coach = coachRepository.findByUuid(uuid)
               .orElseThrow(()-> new AppObjectNotFoundException("Coach", "Ο προπονητής δεν βρέθηκε"));
       if(!coach.getCourses().isEmpty()) {
           Coach coachAdmin = coachRepository.findByRole(RoleType.Admin)
                   .orElseThrow(() -> new AppObjectNotFoundException("Admin", "Ο προπονητής Admin δεν βρέθηκε"));
           List<Course> courses = courseRepository.findCoursesByCoach(coach);
           courses.forEach(course -> {
               course.setCoach(coachAdmin);  // Ή να το ρυθμίσεις σε κάποιον άλλο coach
           });
           courseRepository.saveAll(courses);
       }
       coachRepository.delete(coach);
    }

    /**
     * Ενημερώνει τα στοιχεία σύνδεσης (όνομα χρήστη και κωδικό) ενός προπονητή.
     * Ελέγχει αν ο προπονητής με το συγκεκριμένο UUID υπάρχει στη βάση δεδομένων.
     * Αν δεν βρεθεί, εκτοξεύεται εξαίρεση `AppObjectNotFoundException`.
     * Εάν το όνομα χρήστη που παρέχεται είναι διαφορετικό από το υπάρχον, ενημερώνεται το όνομα χρήστη.
     * Εάν παρέχεται νέος κωδικός, αυτός κωδικοποιείται και ενημερώνεται το πεδίο του κωδικού του προπονητή.
     * Στο τέλος, αποθηκεύεται ο ενημερωμένος προπονητής στη βάση δεδομένων.
     */
    @Transactional(rollbackOn = Exception.class)
   public ApiResponseDTO updateCredentials(NewCredentialsUpdateDTO newCredentialsUpdateDTO)
       throws AppObjectNotFoundException{
        Coach coach = coachRepository.findByUuid(newCredentialsUpdateDTO.getUuid())
                .orElseThrow(()-> new AppObjectNotFoundException("Coach","Ο προπονητής δεν βρέθηκε"));

       if (newCredentialsUpdateDTO.getUsername() != null && !newCredentialsUpdateDTO.getUsername().equals(coach.getUsername())) {
           coach.setUsername(newCredentialsUpdateDTO.getUsername());
       }
       if (newCredentialsUpdateDTO.getPassword() != null) {
           String encodedPassword = passwordEncoder.encode(newCredentialsUpdateDTO.getPassword());
           coach.setPassword(encodedPassword);
       }
       Coach updatedCoach = coachRepository.save(coach);

       return new ApiResponseDTO("confirm update password username");
   }

    @Transactional(rollbackOn = Exception.class)
    public CoachReadOnlyDTO updateCoach(CoachUpdateDTO updateDTO) throws AppObjectNotFoundException {

        // Βρίσκουμε τον Coach με το uuid από το DTO
        Coach coach = coachRepository.findByUuid(updateDTO.getUuid())
                .orElseThrow(() -> new AppObjectNotFoundException("Coach", "Ο προπονητής δεν βρέθηκε"));

        // Ενημερώνουμε τα πεδία του Coach μόνο αν υπάρχουν αλλαγές
        if (updateDTO.getFirstname() != null && !updateDTO.getFirstname().equals(coach.getFirstname())) {
            coach.setFirstname(updateDTO.getFirstname());
        }
        if (updateDTO.getLastname() != null && !updateDTO.getLastname().equals(coach.getLastname())) {
            coach.setLastname(updateDTO.getLastname());
        }
        if (updateDTO.getRole() != null && !updateDTO.getRole().equals(coach.getRole())) {
            coach.setRole(updateDTO.getRole());
        }

        // Βρίσκουμε το ContactDetails με το id
        ContactDetails contactDetails = contactDetailsRepository.findById(updateDTO.getContactDetailsUpdateDTO().getId())
                .orElseThrow(() -> new AppObjectNotFoundException("ContactDetails", "Τα Στοιχεία Επαφής δεν βρέθηκαν"));

        // Ενημερώνουμε τα πεδία του ContactDetails μόνο αν υπάρχουν αλλαγές
        if (updateDTO.getContactDetailsUpdateDTO().getCity() != null && !updateDTO.getContactDetailsUpdateDTO().getCity().equals(contactDetails.getCity())) {
            contactDetails.setCity(updateDTO.getContactDetailsUpdateDTO().getCity());
        }
        if (updateDTO.getContactDetailsUpdateDTO().getRoad() != null && !updateDTO.getContactDetailsUpdateDTO().getRoad().equals(contactDetails.getRoad())) {
            contactDetails.setRoad(updateDTO.getContactDetailsUpdateDTO().getRoad());
        }
        if (updateDTO.getContactDetailsUpdateDTO().getRoad_number() != null && !updateDTO.getContactDetailsUpdateDTO().getRoad_number().equals(contactDetails.getRoad_number())) {
            contactDetails.setRoad_number(updateDTO.getContactDetailsUpdateDTO().getRoad_number());
        }
        if (updateDTO.getContactDetailsUpdateDTO().getPostalCode() != null && !updateDTO.getContactDetailsUpdateDTO().getPostalCode().equals(contactDetails.getPostalCode())) {
            contactDetails.setPostalCode(updateDTO.getContactDetailsUpdateDTO().getPostalCode());
        }
        if (updateDTO.getContactDetailsUpdateDTO().getPhoneNumber() != null && !updateDTO.getContactDetailsUpdateDTO().getPhoneNumber().equals(contactDetails.getPhoneNumber())) {
            contactDetails.setPhoneNumber(updateDTO.getContactDetailsUpdateDTO().getPhoneNumber());
        }

        // Αποθηκεύουμε το ContactDetails
        contactDetails = contactDetailsRepository.save(contactDetails);

        // Ενημερώνουμε το ContactDetails του Coach
        coach.setContactDetails(contactDetails);

        // Αποθηκεύουμε τον Coach στην βάση
        Coach updatedCoach = coachRepository.save(coach);

        // Επιστρέφουμε το CoachReadOnlyDTO με τα ενημερωμένα δεδομένα
        return mapper.mapToCoachReadOnlyDTO(updatedCoach);
    }

    @Transactional
    public Paginated<CoachReadOnlyDTO> getFilteredCoaches(CoachFilters filters) {
        // Εύρεση με φιλτράρισμα και σελιδοποίηση
        var filtered = coachRepository.findAll(getSpecsFromFilters(filters), filters.getPageable());

        List<CoachReadOnlyDTO> coachReadOnlyDTOS = filtered.stream().map(coach -> {
            CoachReadOnlyDTO coachDTO = mapper.mapToCoachReadOnlyDTO(coach);

            List<CourseReadOnlyDTO> courseList = coach.getCourses().stream()
                    .map(mapper::mapToCourseReadOnlyDTO)
                    .toList();

            coachDTO.setCourses(courseList);

            return coachDTO;
        }).toList();

        Pageable pageable = filters.getPageable();
        Page<CoachReadOnlyDTO> page = new PageImpl<>(coachReadOnlyDTOS, pageable, filtered.getTotalElements());

        return new Paginated<>(page);
   }

    @Transactional
    public List<CoachReadOnlyDTO> getAllCoaches() {
        List<Coach> coaches = coachRepository.findAll();
        return coaches.stream()
                .map(coach ->{
                    CoachReadOnlyDTO dto = mapper.mapToCoachReadOnlyDTO(coach);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private Specification<Coach> getSpecsFromFilters(CoachFilters filters){
       return Specification
               .where(CoachSpecification.coachLike(filters.getLastname()))
               .and(CoachSpecification.findByUuid(filters.getUuid()));
   }

}
