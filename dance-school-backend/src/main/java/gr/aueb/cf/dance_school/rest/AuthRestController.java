package gr.aueb.cf.dance_school.rest;

import gr.aueb.cf.dance_school.authentication.AuthenticationService;
import gr.aueb.cf.dance_school.core.exceptions.AppObjectNotAuthorizedException;
import gr.aueb.cf.dance_school.dto.AuthenticationChangeUsernamePasswordDTO;
import gr.aueb.cf.dance_school.dto.AuthenticationRequestDTO;
import gr.aueb.cf.dance_school.dto.AuthenticationResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthRestController {

    private final AuthenticationService authenticationService;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDTO> authenticate(@RequestBody AuthenticationRequestDTO authenticationRequestDTO)
            throws AppObjectNotAuthorizedException {

        AuthenticationResponseDTO authenticationResponseDTO = authenticationService.authenticate(authenticationRequestDTO);
        return new ResponseEntity<>(authenticationResponseDTO, HttpStatus.OK);
    }

    /**
     * Η μέθοδος αυτή επιτρέπει την αλλαγή του username και του κωδικού πρόσβασης του χρήστη.
     */
    @PostMapping("/authenticationChangeUsernamePassword")
    public ResponseEntity<AuthenticationChangeUsernamePasswordDTO> authenticationChangeUsernamePassword
            (@RequestBody AuthenticationRequestDTO authenticationRequestDTO) throws AppObjectNotAuthorizedException{

        AuthenticationChangeUsernamePasswordDTO responseDTO = authenticationService.authenticationChangeUsernamePassword(authenticationRequestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);

    }
}
