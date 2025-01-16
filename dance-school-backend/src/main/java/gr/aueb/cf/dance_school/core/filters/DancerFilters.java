package gr.aueb.cf.dance_school.core.filters;

import jakarta.annotation.Nullable;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class DancerFilters extends GenericFilters{

    @Nullable
    private String lastname;
    @Nullable
    private LocalDate dateOfBirth;
    @Nullable
    private LocalDate contract_end_from;
    @Nullable
    private LocalDate contract_end_until;
    @Nullable
    private Boolean isUnder18;
    @Nullable
    private Long courseId;
    @Nullable
    private String coachUuid;
}
