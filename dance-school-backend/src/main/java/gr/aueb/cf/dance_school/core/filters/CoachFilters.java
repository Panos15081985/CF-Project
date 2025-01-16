package gr.aueb.cf.dance_school.core.filters;

import jakarta.annotation.Nullable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CoachFilters extends GenericFilters {

    @Nullable
    private String uuid;

    @Nullable
    private String lastname;
}
