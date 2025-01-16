package gr.aueb.cf.dance_school.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewCredentialsUpdateDTO {

    private String username;
    private String password;
    private String uuid;
}
