package gr.aueb.cf.dance_school.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseErrorMessage {

    private String code;
    private String description;


}
