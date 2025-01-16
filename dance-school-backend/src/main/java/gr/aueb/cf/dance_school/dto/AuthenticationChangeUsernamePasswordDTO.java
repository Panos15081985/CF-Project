package gr.aueb.cf.dance_school.dto;
import lombok.Getter;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
public class AuthenticationChangeUsernamePasswordDTO {

    private String message;
    private Timestamp timestamp;
    private Boolean accept;

    public AuthenticationChangeUsernamePasswordDTO(String message, Boolean accept ) {
        this.message = message;
        this.accept = accept;
        this.timestamp = Timestamp.valueOf(LocalDateTime.now());;
    }
}
