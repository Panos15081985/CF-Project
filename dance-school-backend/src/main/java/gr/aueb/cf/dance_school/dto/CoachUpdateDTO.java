package gr.aueb.cf.dance_school.dto;

import gr.aueb.cf.dance_school.core.enums.RoleType;
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
public class CoachUpdateDTO {

    @NotEmpty(message = "firstname must not be empty")
    private String firstname;

    @NotEmpty(message = "lastname must not be empty")
    private String lastname;

    @NotNull(message = "role must be not null")
    private RoleType role;

    @NotNull(message = "uuid must be not null")
    private String uuid;

    private ContactDetailsUpdateDTO contactDetailsUpdateDTO;
}
