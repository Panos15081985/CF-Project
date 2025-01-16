package gr.aueb.cf.dance_school.core.filters;

import jakarta.annotation.Nullable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CourseFilters extends GenericFilters {

    @Nullable
    private String day;
    @Nullable
    private String name;
    @Nullable
    private Long coachId;
}
