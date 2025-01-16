package gr.aueb.cf.dance_school.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

/**
 * Ο `CustomAccessDeniedHandler` είναι υπεύθυνος για την επεξεργασία των περιπτώσεων όπου ο χρήστης δεν έχει άδεια να αποκτήσει πρόσβαση σε ένα συγκεκριμένο endpoint.
 * Όταν εντοπίζεται ένα σφάλμα "Access Denied", η μέθοδος `handle` επιστρέφει μια απάντηση με τον HTTP status κωδικό 403 (FORBIDDEN) και ένα JSON μήνυμα που εξηγεί την αποτυχία.
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=utf-8");
        String json = "{\"code\":\"userNotAuthorized\",\"description\":\"Ο χρήστης δεν επιτρέπεται να έχει πρόσβαση σε αυτό το endpoint\"}";
        response.getWriter().write(json);
    }
}

