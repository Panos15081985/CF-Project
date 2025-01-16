package gr.aueb.cf.dance_school.dto;

import gr.aueb.cf.dance_school.core.enums.GenderType;
import gr.aueb.cf.dance_school.models.Course;
import gr.aueb.cf.dance_school.models.Guardian;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DancerReadOnlyDTO {

    private Long id;
    private String uuid;
    private String firstname;
    private String lastname;
    private LocalDate dateOfBirth;
    private GenderType gender;
    private ContactDetailsReadOnlyDTO contactDetailsReadOnlyDTO;
    private GuardianReadOnlyDTO guardianReadOnlyDTO;
    private LocalDate contractEnd;
    private List<CourseReadOnlyDTO> courseList;
}
