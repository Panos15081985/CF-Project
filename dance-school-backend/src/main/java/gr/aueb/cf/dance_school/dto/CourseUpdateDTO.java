package gr.aueb.cf.dance_school.dto;

import gr.aueb.cf.dance_school.core.enums.DayCourse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CourseUpdateDTO {

    @NotNull(message = "id must not be empty")
    private Long id;

    @NotEmpty(message = "name must not be empty")
    private String name;

    @NotEmpty(message = "Description must not be empty")
    private String description;

    @NotNull(message = "Day must not be null")
    private DayCourse day;

    @NotEmpty(message = "Time must not be empty")
    private String startTime;

    @NotEmpty(message = "Time must not be empty")
    private String endTime;

    @NotEmpty(message = "CoachUuid must not be empty")
    private String CoachUuid;
}
