package gr.aueb.cf.dance_school.dto;

import gr.aueb.cf.dance_school.core.enums.RoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CoachInsertDTO {

    @NotEmpty(message = "firstname must not be empty")
    private String firstname;

    @NotEmpty(message = "lastname must not be empty")
    private String lastname;

    @NotEmpty(message = "lastname must not be empty")
    private String username;

    @Pattern(regexp = "^(?=.*?[a-z])(?=.*?[A-Z])(?=.*?[0-9])(?=.*?[@#$!&?]).{8,}$",message = "invalid Password")
    private String password;

    @NotNull(message = "role must be not null")
    private RoleType role;


    private ContactDetailsInsertDTO contactDetailsInsertDTO;


    @Override
    public String toString() {
        return "CoachInsertDTO{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +

                ", addressInsertDTO=" + contactDetailsInsertDTO +
                '}';
    }
}
