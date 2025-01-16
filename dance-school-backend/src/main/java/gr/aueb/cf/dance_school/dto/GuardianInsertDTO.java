package gr.aueb.cf.dance_school.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GuardianInsertDTO {

    @NotEmpty(message = "firstname must not be empty")
    private String firstname;

    @NotEmpty(message = "lastname must not be empty")
    private String lastname;


}
