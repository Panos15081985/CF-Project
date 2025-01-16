package gr.aueb.cf.dance_school.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

/**
 * Ο `JwtService` είναι υπεύθυνος για τη δημιουργία και την επαλήθευση των JWT (JSON Web Tokens) για την αυθεντικοποίηση και την εξουσιοδότηση χρηστών στην εφαρμογή.
 * Χρησιμοποιεί το `secretKey` για την υπογραφή των JWT και το `jwtExpiration` για να καθορίσει τη διάρκεια ισχύος των tokens.
 * Η κλάση παρέχει διάφορες μεθόδους:
 * - `generateToken`: Δημιουργεί ένα νέο JWT για τον χρήστη με το όνομα χρήστη, τον ρόλο και το UUID του.
 * - `isTokenValid`: Επαληθεύει αν το JWT είναι έγκυρο και αν ταιριάζει με τα στοιχεία του χρήστη.
 * - `getStringClaim`: Εξάγει μια συγκεκριμένη αξίωση (claim) από το JWT.
 * - `extractSubject`: Εξάγει το υποκείμενο (subject) από το JWT.
 * - `extractClaim`: Εξάγει μια συγκεκριμένη αξίωση από το JWT χρησιμοποιώντας έναν επεξεργαστή αξιώσεων.
 * - `isTokenExpired`: Ελέγχει αν το JWT έχει λήξει.
 * - `extractExpiration`: Εξάγει την ημερομηνία λήξης του JWT.
 * - `extractAllClaims`: Εξάγει όλες τις αξιώσεις από το JWT.
 * - `getSignInKey`: Επιστρέφει το κλειδί υπογραφής από το `secretKey` για τη δημιουργία του JWT.
 */

 @Service
public class JwtService {

    private String secretKey = "5ce98d378ec88ea09ba8bcd511ef23645f04cc8e70b9134b98723a53c275bbc5";
    private long jwtExpiration = 10800000;  // 3 hours in milliseconds

    public String generateToken(String username, String role, String uuid) {
        var claims = new HashMap<String, Object>();
        claims.put("role", role);
        claims.put("uuid",uuid);
        return Jwts
                .builder()
                .setIssuer("self")
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String subject = extractSubject(token);
        return (subject.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public String getStringClaim(String token, String claim) {
        return extractAllClaims(token).get(claim, String.class);
    }

    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
