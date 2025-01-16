package gr.aueb.cf.dance_school.dto;

import gr.aueb.cf.dance_school.core.enums.RoleType;
import gr.aueb.cf.dance_school.models.Coach;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CoachReadOnlyDTO {

    private Long id;
    private String firstname;
    private String uuid;
    private String lastname;
    private RoleType role;
    private ContactDetailsReadOnlyDTO contactDetailsReadOnlyDTO;
    private List<CourseReadOnlyDTO> courses;





}
