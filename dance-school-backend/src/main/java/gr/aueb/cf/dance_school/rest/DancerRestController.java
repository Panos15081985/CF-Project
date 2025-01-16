package gr.aueb.cf.dance_school.rest;

import gr.aueb.cf.dance_school.core.exceptions.*;
import gr.aueb.cf.dance_school.core.filters.CoachFilters;
import gr.aueb.cf.dance_school.core.filters.DancerFilters;
import gr.aueb.cf.dance_school.core.filters.Paginated;
import gr.aueb.cf.dance_school.dto.*;
import gr.aueb.cf.dance_school.service.DancerService;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dancer")
public class DancerRestController {

    private final DancerService dancerService;
    private static final Logger LOGGER = LoggerFactory.getLogger(CoachRestController.class);

    @PostMapping("/save")
    public ResponseEntity<DancerReadOnlyDTO> dancerSave(
            @Valid @RequestBody DancerInsertDTO dancerInsertDTO,
            BindingResult bindingResult) throws AppObjectAlreadyExistsException, ValidationException {

        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        try {
            DancerReadOnlyDTO dancerReadOnlyDTO = dancerService.saveDancer(dancerInsertDTO);
            return new ResponseEntity<>(dancerReadOnlyDTO, HttpStatus.OK);

        } catch (AppObjectAlreadyExistsException e) {
            LOGGER.error("ERROR: Dancer can not get upload.", e);
            throw new AppObjectAlreadyExistsException("Dancer", "Ο χορευτής υπάρχει ήδη.");
        } catch (ValidationException | ConflictException | AppObjectNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/delete{uuid}")
    public ResponseEntity<ApiResponseDTO> deleteDancer (@PathVariable String uuid)
        throws AppObjectNotFoundException{

        try{
            dancerService.deleteDancer(uuid);
            LOGGER.info("INFO: Dancer with uuid {} is deleted.", uuid);
            return ResponseEntity.ok(new ApiResponseDTO("dancer deleted"));
        }catch (AppObjectNotFoundException e){
            LOGGER.error("ERROR: Dancer with uuid {} not found.", uuid, e);
            throw new AppObjectNotFoundException("Dancer", "Ο χορευτής υπάρχει ήδη.");
        }
    }

    @PostMapping("/filtered")
    public ResponseEntity<Paginated<DancerReadOnlyDTO>> getDancersFilteredPaginated (
            @Nullable @RequestBody DancerFilters filters){
        try{
            if(filters == null) filters = DancerFilters.builder().build();
            return ResponseEntity.ok(dancerService.getDancerFilteredPaginated(filters));
        }catch (Exception e){
            LOGGER.error("ERROR: Could not get teachers.", e);
            throw e;
        }
    }

    @PostMapping("/update")
    public ResponseEntity<DancerReadOnlyDTO> updateDancer(
            @Valid @RequestBody DancerUpdateDTO updateDTO,
            BindingResult bindingResult
    )throws AppObjectNotFoundException, ValidationException {
        System.out.println(updateDTO);
        if(bindingResult.hasErrors()){
            throw  new ValidationException(bindingResult);
        }
        try{
            DancerReadOnlyDTO dancerReadOnlyDTO = dancerService.updateDancer(updateDTO);
            return new ResponseEntity<>(dancerReadOnlyDTO, HttpStatus.OK);
        }catch (AppObjectNotFoundException e){
            LOGGER.error("ERROR: Dancer can not update.", e);
            throw new AppObjectNotFoundException("Coach", "Ο προπονητής δεν βρέθηκε.");
        }
    }
}
