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
public class ContactDetailsUpdateDTO {

    @NotEmpty(message = "id must not be empty")
    private Long id;

    @NotEmpty(message = "City must not be empty")
    private String city;
    private String road_number;
    private String road;
    private String postalCode;

    @NotEmpty(message = "Email must not be empty")
    @Email(message = "invalid Email")
    private String email;

    @NotEmpty(message = "PHoneNumber must not be empty")
    private String phoneNumber;
}
