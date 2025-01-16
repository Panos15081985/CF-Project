package gr.aueb.cf.dance_school.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ApiResponseDTO {
    private String message;
    private LocalDateTime timeStamp;

    public ApiResponseDTO(String message) {

        this.message = message;
        this.timeStamp = LocalDateTime.now();
    }


}
