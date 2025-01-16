package gr.aueb.cf.dance_school.dto;

import gr.aueb.cf.dance_school.core.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponseDTO {

    private String firstname;
    private RoleType role;
    private String uuid;
    private String token;



}
