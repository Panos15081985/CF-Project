package gr.aueb.cf.dance_school.core;
import gr.aueb.cf.dance_school.core.exceptions.*;
import gr.aueb.cf.dance_school.dto.ResponseErrorMessage;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Η κλάση `ErrorHandler` παρέχει παγκόσμια διαχείριση σφαλμάτων για τις εξαιρέσεις που εμφανίζονται στην εφαρμογή.
 * Είναι διακοσμημένη με την αναλυτική δήλωση `@ControllerAdvice`, η οποία επιτρέπει τη διαχείριση σφαλμάτων σε ολόκληρη την εφαρμογή.
 * Οι μέθοδοι χειρίζονται εξαιρέσεις και επιστρέφουν κατάλληλες απαντήσεις HTTP με πληροφορίες σφάλματος.
 */
@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String,String>> handleValidationException(ValidationException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        Map<String,String> errors = new HashMap<>();
        for(FieldError fieldError : bindingResult.getFieldErrors()){
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AppObjectNotFoundException.class)
    public ResponseEntity<ResponseErrorMessage> handleConstraintViolationException(AppObjectNotFoundException e) {
        return new ResponseEntity<>(new ResponseErrorMessage(e.getCode(), e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AppObjectAlreadyExistsException.class)
    public ResponseEntity<ResponseErrorMessage> handleConstraintViolationException(AppObjectAlreadyExistsException e) {
        return new ResponseEntity<>(new ResponseErrorMessage(e.getCode(), e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AppObjectNotAuthorizedException.class)
    public ResponseEntity<ResponseErrorMessage> handleConstraintViolationException(AppObjectNotAuthorizedException e) {
        return new ResponseEntity<>(new ResponseErrorMessage(e.getCode(), e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ResponseErrorMessage> handleConflictException(ConflictException e){
        return new ResponseEntity<>(new ResponseErrorMessage(e.getCode(), e.getMessage()),HttpStatus.FORBIDDEN);
    }
}
