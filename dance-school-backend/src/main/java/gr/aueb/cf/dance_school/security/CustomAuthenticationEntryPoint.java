package gr.aueb.cf.dance_school.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * Ο `CustomAuthenticationEntryPoint` είναι υπεύθυνος για την επεξεργασία των περιπτώσεων όπου ο χρήστης προσπαθεί να αποκτήσει πρόσβαση σε ένα προστατευμένο endpoint χωρίς να είναι πιστοποιημένος (δηλαδή χωρίς έγκυρο διακριτικό ή cookie).
 * Όταν προκύπτει μια εξαίρεση αυθεντικοποίησης (AuthenticationException), η μέθοδος `commence` επιστρέφει μια απάντηση με τον HTTP status κωδικό 401 (UNAUTHORIZED) και ένα JSON μήνυμα που εξηγεί την ανάγκη πιστοποίησης για την πρόσβαση.
 */

 public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        //set response 401
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        String json = "{\"code\":\"userNotAuthenticated\",\"description\":\"Ο χρήστης πρέπει να είναι πιστοποιημένος για να έχει πρόσβαση σε αυτό το endpoint.\"}";
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);

    }
}


