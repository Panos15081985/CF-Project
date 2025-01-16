package gr.aueb.cf.dance_school.rest;

import gr.aueb.cf.dance_school.core.exceptions.AppObjectAlreadyExistsException;
import gr.aueb.cf.dance_school.core.exceptions.AppObjectNotFoundException;
import gr.aueb.cf.dance_school.core.exceptions.ValidationException;
import gr.aueb.cf.dance_school.core.filters.CoachFilters;
import gr.aueb.cf.dance_school.core.filters.Paginated;
import gr.aueb.cf.dance_school.dto.*;
import gr.aueb.cf.dance_school.service.CoachService;
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
@RequestMapping("coach")
public class CoachRestController {

    private final CoachService coachService;
    private static final Logger LOGGER = LoggerFactory.getLogger(CoachRestController.class);

    @PostMapping("/save")
    public ResponseEntity<CoachReadOnlyDTO> saveCoach(
            @Valid @RequestBody CoachInsertDTO coachInsertDTO,
            BindingResult bindingResult
    ) throws AppObjectAlreadyExistsException, ValidationException {

        if(bindingResult.hasErrors()){
            throw new ValidationException(bindingResult);
        }
        try{
            CoachReadOnlyDTO coachReadOnlyDTO = coachService.saveCoach(coachInsertDTO);
            return new ResponseEntity<>(coachReadOnlyDTO, HttpStatus.OK);

        }catch (Exception e){
            LOGGER.error("ERROR: Coach can not get upload.", e);
            throw e;
        }

    }

    @DeleteMapping("/delete/{uuid}")
    public ResponseEntity<ApiResponseDTO> deleteCoach(@PathVariable String uuid)
            throws AppObjectNotFoundException {
        try {
            coachService.deleteCoach(uuid);
            LOGGER.info("INFO: Coach with uuid {} is deleted", uuid);
            return ResponseEntity.ok(new ApiResponseDTO("Coach with uuid " + uuid + " is deleted"));
        } catch (AppObjectNotFoundException e) {
            LOGGER.error("ERROR: Coach with uuid {} not found.", uuid, e);
            throw new AppObjectNotFoundException("Coach", "Ο προπονητής δεν βρέθηκε.");
        }
    }

    @PostMapping("/filtered")
    public ResponseEntity<Paginated<CoachReadOnlyDTO>> getCoachesFilteredPaginated (
            @Nullable @RequestBody CoachFilters filters){
        try{
            if(filters == null) filters = CoachFilters.builder().build();
            return ResponseEntity.ok(coachService.getFilteredCoaches(filters));
        }catch (Exception e){
            LOGGER.error("ERROR: Could not get teachers.", e);
            throw e;
        }
    }

    @PostMapping("/update")
    public ResponseEntity<CoachReadOnlyDTO> updateCoach(
            @Valid @RequestBody CoachUpdateDTO updateDTO,
            BindingResult bindingResult
    )throws AppObjectNotFoundException, ValidationException {
        if(bindingResult.hasErrors()){
            throw  new ValidationException(bindingResult);
        }
        try{
            CoachReadOnlyDTO coachReadOnlyDTO = coachService.updateCoach(updateDTO);
            LOGGER.info("INFO.: Credentials are change");
            return new ResponseEntity<>(coachReadOnlyDTO, HttpStatus.OK);
        }catch (AppObjectNotFoundException e){
            LOGGER.error("ERROR: Course can not update.", e);
            throw new AppObjectNotFoundException("Coach", "Ο προπονητής δεν βρέθηκε.");
        }
    }

/**
 * Η μέθοδος `updateCredentials` επιτρέπει την ενημέρωση των στοιχείων σύνδεσης ενός προπονητή.
 * Ελέγχει πρώτα για σφάλματα στη φόρμα και στη συνέχεια ενημερώνει τα στοιχεία, επιστρέφοντας το αποτέλεσμα.
 * Σε περίπτωση που ο προπονητής δεν βρεθεί, αναγγέλλεται εξαίρεση `AppObjectNotFoundException`.
 */
 @PostMapping("/update/credentials")
    public ResponseEntity<ApiResponseDTO> updateCredentials(
            @Valid @RequestBody NewCredentialsUpdateDTO newCredentialsUpdateDTO,
            BindingResult bindingResult
    )throws AppObjectNotFoundException, ValidationException {
        if(bindingResult.hasErrors()){
            throw  new ValidationException(bindingResult);
        }
        try{
            ApiResponseDTO apiResponseDTO = coachService.updateCredentials(newCredentialsUpdateDTO);
            LOGGER.info("INFO.: Successfully updated credentials for Coach with uuid:" + newCredentialsUpdateDTO.getUuid() );
            return new ResponseEntity<>(apiResponseDTO, HttpStatus.OK);
        }catch (AppObjectNotFoundException e){
            LOGGER.error("ERROR: Credentials can not update.", e);
            throw new AppObjectNotFoundException("Coach", "Ο προπονητής δεν βρέθηκε.");
        }
    }

    @GetMapping("/getAllCoaches")
    public ResponseEntity<List<CoachReadOnlyDTO>> allCourses() {
        List<CoachReadOnlyDTO> coaches = coachService.getAllCoaches();
        return ResponseEntity.ok(coaches);
    }
}
