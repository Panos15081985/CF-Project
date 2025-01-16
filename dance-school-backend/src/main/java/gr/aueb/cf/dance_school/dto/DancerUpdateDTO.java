package gr.aueb.cf.dance_school.dto;

import gr.aueb.cf.dance_school.core.enums.GenderType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DancerUpdateDTO {

    @NotEmpty(message = "uuid must not be empty")
    private String uuid;

    @NotEmpty(message = "firstname must not be empty")
    private String firstname;

    @NotEmpty(message = "lastname must not be empty")
    private String lastname;

    @NotNull(message = "dateOf Birth must be not null")
    private LocalDate dateOfBirth;

    @NotNull(message = "gender must be null")
    private GenderType gender;

    private LocalDate contractEnd;

    private ContactDetailsUpdateDTO contactDetailsUpdateDTO;

    private GuardianUpdateDTO guardianUpdateDTO;

    @NotNull(message = "courses must be null")
    private List<Long> courseIds;
}
