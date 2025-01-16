package gr.aueb.cf.dance_school.authentication;

import gr.aueb.cf.dance_school.security.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Φίλτρο αυθεντικοποίησης που εκτελείται μία φορά ανά αίτημα.
 * Υπεύθυνο για την επεξεργασία του JWT που περιλαμβάνεται στην κεφαλίδα Authorization
 * και τη ρύθμιση του SecurityContext αν το JWT είναι έγκυρο.
 */

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtService jwtService;//JwtService: Χρησιμοποιείται για την εξαγωγή στοιχείων από το JWT και τον έλεγχο εγκυρότητάς του.
    private final UserDetailsService userDetailsService;//UserDetailsService: Παρέχει πρόσβαση σε στοιχεία του χρήστη που είναι συνδεδεμένα με το JWT.

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String jwt;
        String username;
        String userRole;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        try{
            username = jwtService.extractSubject(jwt);
            userRole = jwtService.getStringClaim(jwt, "role");

            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if(jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                }else {
                    LOGGER.warn("Token is not valid" + request.getRequestURI());
                }
            }
        }catch (ExpiredJwtException e){
            LOGGER.warn("Token is expired", e);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json;charset=UTF-8");
            String jsonBody = "{\"code\":\"expired token\",\"description\":\"Η Σύνδεση έχει λήξει. Παρακαλώ συνδεθείτε ξανά.\"}";
            response.getWriter().write(jsonBody);
            return;
        }catch (Exception e){
            LOGGER.warn("WARN: Something went wrong while parsing JWT", e);
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType("application/json");
            String jsonBody = "{\"code\":\"invalidToken\",\"description\"Κάτι πήγε στραβά\"}";
            response.getWriter().write(jsonBody);
            return;
        }
        filterChain.doFilter(request, response);
    }
}

