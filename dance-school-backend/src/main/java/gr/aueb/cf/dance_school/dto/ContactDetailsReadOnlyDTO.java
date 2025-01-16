package gr.aueb.cf.dance_school.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ContactDetailsReadOnlyDTO {

    private Long id;
    private String city;
    private String road;
    private String road_number;
    private String postalCode;
    private String Email;
    private String PhoneNumber;
}
