package gr.aueb.cf.dance_school.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GuardianReadOnlyDTO {

    private Long id;
    private String firstname;
    private String lastname;

}
