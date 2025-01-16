package gr.aueb.cf.dance_school.service;

import gr.aueb.cf.dance_school.core.exceptions.*;
import gr.aueb.cf.dance_school.core.filters.CourseFilters;
import gr.aueb.cf.dance_school.core.filters.DancerFilters;
import gr.aueb.cf.dance_school.core.filters.Paginated;
import gr.aueb.cf.dance_school.core.specification.CourseSpecification;
import gr.aueb.cf.dance_school.core.specification.DancerSpecification;
import gr.aueb.cf.dance_school.dto.*;
import gr.aueb.cf.dance_school.mapper.Mapper;
import gr.aueb.cf.dance_school.models.*;
import gr.aueb.cf.dance_school.repository.ContactDetailsRepository;
import gr.aueb.cf.dance_school.repository.CourseRepository;
import gr.aueb.cf.dance_school.repository.DancerRepository;
import gr.aueb.cf.dance_school.repository.GuardianRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DancerService {
    private final DancerRepository dancerRepository;
    private final CourseRepository courseRepository;
    private final GuardianRepository guardianRepository;
    private final ContactDetailsRepository contactDetailsRepository;
    private final Mapper mapper;

    @Transactional(rollbackOn = Exception.class)
    public DancerReadOnlyDTO saveDancer(DancerInsertDTO dancerInsertDTO)
            throws AppObjectAlreadyExistsException, ConflictException, ValidationException, AppObjectNotFoundException {

        // 1. Έλεγχος ηλικίας του χορευτή
        validateDancerAge(dancerInsertDTO);

        // 2. Έλεγχος για διπλότυπο χορευτή
        if (isDancerDuplicate(dancerInsertDTO)) {
            throw new AppObjectAlreadyExistsException("Dancer", "Ο χορευτής υπάρχει ήδη.");
        }

        // 3. Μετατροπή DTO σε οντότητα
        Dancer dancer = mapper.mapToDancerEntity(dancerInsertDTO);

        // 4. Αντιστοίχιση μαθημάτων
        assignCoursesToDancer(dancer, dancerInsertDTO.getCourseIds());

        // 5. Διαχείριση του κηδεμόνα
        manageGuardianForDancer(dancer, dancerInsertDTO.getGuardianInsertDTO());

        // 6. Αποθήκευση του χορευτή
        Dancer savedDancer = dancerRepository.save(dancer);

        // 7. Επιστροφή DTO για τον αποθηκευμένο χορευτή
        return mapper.mapToDancerReadOnlyDTO(savedDancer);
    }

    private void validateDancerAge(DancerInsertDTO dancerInsertDTO) throws ValidationException, ConflictException {
        if (dancerInsertDTO.getDateOfBirth() == null) {
            throw new ConflictException("Dancer","Η ημερομηνία γέννησης δεν μπορεί να είναι κενή.");
        }
        // Υπολογισμός ηλικίας
        LocalDate today = LocalDate.now();
        Period age = Period.between(dancerInsertDTO.getDateOfBirth(), today);

        if (age.getYears() < 18 && dancerInsertDTO.getGuardianInsertDTO() == null) {
            throw new ConflictException("Dancer","Χορευτής κάτω των 18 ετών πρέπει να έχει κηδεμόνα.");
        }
    }

    private boolean isDancerDuplicate(DancerInsertDTO dto) {
        return dancerRepository.existsByFirstnameAndLastnameAndDateOfBirth(
                dto.getFirstname(),
                dto.getLastname(),
                dto.getDateOfBirth()
        );
    }

    private void assignCoursesToDancer(Dancer dancer, List<Long> courseIds) throws ConflictException, AppObjectNotFoundException {
        if (courseIds == null || courseIds.isEmpty()) {
            throw new ConflictException("Dancer", "Ο χορευτής πρέπει να έχει τουλάχιστον ένα μάθημα.");
        }

        Set<Course> courses = new HashSet<>();
        for (Long courseId : courseIds) {
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new AppObjectNotFoundException("Course","Το μάθημα δεν βρέθηκε."));
            courses.add(course);
        }
        dancer.setCourses(courses);
    }

    private void manageGuardianForDancer(Dancer dancer, GuardianInsertDTO guardianInsertDTO) {
        if (guardianInsertDTO != null) {
            Guardian guardian = guardianRepository.findByFirstnameAndLastname(
                            guardianInsertDTO.getFirstname(),
                            guardianInsertDTO.getLastname())
                    .orElseGet(() -> {
                        // Δημιουργία νέου κηδεμόνα αν δεν υπάρχει
                        Guardian newGuardian = mapper.mapToGuardianEntity(guardianInsertDTO);
                        return guardianRepository.save(newGuardian);
                    });

            // Σύνδεση του χορευτή με τον κηδεμόνα
            guardian.getDancers().add(dancer);
            dancer.setGuardian(guardian);
        } else {
            // Αν ο κηδεμόνας είναι null, το πεδίο guardian_id παραμένει null
            dancer.setGuardian(null);
        }
    }

    public void deleteDancer (String uuid) throws AppObjectNotFoundException{
        Dancer dancer = dancerRepository.findByUuid(uuid)
                .orElseThrow(()->new AppObjectNotFoundException("Dancer", "Ο χορευτής δεν βρέθηκε."));

        // Αν ο χορευτής είναι εγγεγραμμένος σε κάποιο μάθημα, αφαιρούμε τον χορευτή από τα μαθήματα.
        if(dancer.getCourses() != null && !dancer.getCourses().isEmpty()){
            for(Course course : dancer.getCourses()){
                course.getDancers().remove(dancer);
            }
            dancer.getCourses().clear();
        }
        // Αναζητούμε τον γονέα του χορευτή. Αν υπάρχει, αφαιρούμε τον χορευτή από τη λίστα των χορευτών του γονέα.
        Guardian guardian = dancer.getGuardian();
        if(guardian != null){
            guardian.getDancers().remove(dancer);

            if(guardian.getDancers().isEmpty()){
                guardianRepository.delete(guardian);
            }
        }
        dancerRepository.delete(dancer);
    }

    @Transactional
    public Paginated<DancerReadOnlyDTO> getDancerFilteredPaginated(DancerFilters filters){
        var filtered = dancerRepository.findAll(getSpecsFromFilters(filters),filters.getPageable());
        List<DancerReadOnlyDTO> dancerReadOnlyDTOS = filtered.stream().map(dancer -> {
            DancerReadOnlyDTO dancerDTO = mapper.mapToDancerReadOnlyDTO(dancer);

            List<CourseReadOnlyDTO> courseList = dancer.getCourses().stream()
                    .map(mapper::mapToCourseReadOnlyDTO)
                    .toList();
            dancerDTO.setCourseList(courseList);

            return dancerDTO;
        }).toList();
        Pageable pageable = filters.getPageable();
        Page<DancerReadOnlyDTO> page = new PageImpl<>(dancerReadOnlyDTOS, pageable, filtered.getTotalElements());

        return new Paginated<>(page);
    }

    private Specification<Dancer> getSpecsFromFilters(DancerFilters filters) {
        return Specification
                .where(DancerSpecification.lastnameLike(filters.getLastname()))
                .and(DancerSpecification.byDateOfBirth(filters.getDateOfBirth()))
                .and(DancerSpecification.byIsUnder18(filters.getIsUnder18()))
                .and(DancerSpecification.byCourseId(filters.getCourseId()))
                .and(DancerSpecification.byCoachUuid(filters.getCoachUuid()))
                .and(DancerSpecification.byPeriodContractEnd(filters.getContract_end_from(), filters.getContract_end_until()));
    }


    @Transactional(rollbackOn = Exception.class)
    public DancerReadOnlyDTO updateDancer(DancerUpdateDTO updateDTO) throws AppObjectNotFoundException {

        // Βρίσκουμε τον Coach με το uuid από το DTO
        Dancer dancer = dancerRepository.findByUuid(updateDTO.getUuid())
                .orElseThrow(() -> new AppObjectNotFoundException("Dancer", "Ο Χορευτής δεν βρέθηκε."));

        // Ενημερώνουμε τα πεδία του Coach μόνο αν υπάρχουν αλλαγές
        if (updateDTO.getFirstname() != null && !updateDTO.getFirstname().equals(dancer.getFirstname())) {
            dancer.setFirstname(updateDTO.getFirstname());
        }
        if (updateDTO.getLastname() != null && !updateDTO.getLastname().equals(dancer.getLastname())) {
            dancer.setLastname(updateDTO.getLastname());
        }
        if (updateDTO.getDateOfBirth() != null && !updateDTO.getDateOfBirth().equals(dancer.getDateOfBirth())) {
            dancer.setDateOfBirth(updateDTO.getDateOfBirth());
        }
        if (updateDTO.getGender() != null && !updateDTO.getGender().equals(dancer.getGenderType())) {
            dancer.setGenderType(updateDTO.getGender());
        }
        if (updateDTO.getDateOfBirth() != null && !updateDTO.getDateOfBirth().equals(dancer.getDateOfBirth())) {
            dancer.setDateOfBirth(updateDTO.getDateOfBirth());
        }
        if (updateDTO.getContractEnd() != null && !updateDTO.getContractEnd().equals(dancer.getContractEnd())){
            dancer.setContractEnd(updateDTO.getContractEnd());
        }

        // Βρίσκουμε το ContactDetails με το id
        ContactDetails contactDetails = contactDetailsRepository.findById(updateDTO.getContactDetailsUpdateDTO().getId())
                .orElseThrow(() -> new AppObjectNotFoundException("ContactDetails", "Τα στοιχεία επαφής δεν βρέθηκαν."));

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

        // Ενημερώνουμε το ContactDetails του Dancer
        dancer.setContactDetails(contactDetails);

        // Ενημερώνουμε γονέα αν υπάρχει
        if (updateDTO.getGuardianUpdateDTO() != null) {

            Guardian guardian = guardianRepository.findById(updateDTO.getGuardianUpdateDTO().getId())
                    .orElseThrow(()-> new AppObjectNotFoundException("Guardian", "Ο κηδεμόνας δεν βρέθηκε."));

            if (updateDTO.getGuardianUpdateDTO().getFirstname() != null && !updateDTO.getGuardianUpdateDTO().getFirstname().equals(dancer.getGuardian().getFirstname())) {
                guardian.setFirstname(updateDTO.getGuardianUpdateDTO().getFirstname());
            }
            if (updateDTO.getGuardianUpdateDTO().getLastname() != null && !updateDTO.getGuardianUpdateDTO().getLastname().equals(dancer.getGuardian().getLastname())) {
                guardian.setLastname(updateDTO.getGuardianUpdateDTO().getLastname());
            }
            dancer.setGuardian(guardian);
        }
        Dancer updatedDancer = dancerRepository.save(dancer);

        return mapper.mapToDancerReadOnlyDTO(updatedDancer);
    }
}
