package gr.aueb.cf.dance_school.dto;

import gr.aueb.cf.dance_school.core.enums.DayCourse;
import gr.aueb.cf.dance_school.core.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CourseReadOnlyDTO {

    private Long id;
    private String name;
    private String description;
    private DayCourse day;
    private String startTime;
    private String endTime;
    private String firstnameLastnameCoach;
    private RoleType role;
    private String CoachUuid;


}
