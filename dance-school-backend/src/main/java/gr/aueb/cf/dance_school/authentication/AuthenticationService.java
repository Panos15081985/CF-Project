package gr.aueb.cf.dance_school.authentication;

import gr.aueb.cf.dance_school.core.exceptions.AppObjectNotAuthorizedException;
import gr.aueb.cf.dance_school.dto.AuthenticationChangeUsernamePasswordDTO;
import gr.aueb.cf.dance_school.dto.AuthenticationRequestDTO;
import gr.aueb.cf.dance_school.dto.AuthenticationResponseDTO;
import gr.aueb.cf.dance_school.models.Coach;
import gr.aueb.cf.dance_school.repository.CoachRepository;
import gr.aueb.cf.dance_school.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * Υπηρεσία υπεύθυνη για τη διαχείριση της αυθεντικοποίησης χρηστών.
 * Παρέχει μεθόδους για έλεγχο διαπιστευτηρίων και διαχείριση εξουσιοδότησης.
 */

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CoachRepository coachRepository;

    /**
     * Αυθεντικοποιεί έναν χρήστη με βάση τα διαπιστευτήριά του
     * και επιστρέφει ένα token μαζί με τις πληροφορίες του προπονητή.
     */
    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO dto)
        throws AppObjectNotAuthorizedException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
        Coach coach = coachRepository.findByUsername(authentication.getName())
                .orElseThrow(()-> new AppObjectNotAuthorizedException("Coach", "Ο προπονητής δεν είναι εξουσιοδοτημένος.")
                );
        String token = jwtService.generateToken(authentication.getName(),coach.getRole().name(), coach.getUuid());
        return new AuthenticationResponseDTO(coach.getFirstname(),coach.getRole(), coach.getUuid(),token);
    }

    /**
     * Ελέγχει την αυθεντικότητα ενός χρήστη με βάση τα διαπιστευτήρια του
     * και επιστρέφει επιβεβαίωση της αυθεντικοποίησης.
     **/
    public AuthenticationChangeUsernamePasswordDTO authenticationChangeUsernamePassword(AuthenticationRequestDTO dto)
        throws AppObjectNotAuthorizedException{
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
        Coach coach = coachRepository.findByUsername(authentication.getName())
                .orElseThrow(()-> new AppObjectNotAuthorizedException("Coach", "Ο προπονητής δεν είναι εξουσιοδοτημένος."));
        return new AuthenticationChangeUsernamePasswordDTO("Authenticate",true);
    }
}
