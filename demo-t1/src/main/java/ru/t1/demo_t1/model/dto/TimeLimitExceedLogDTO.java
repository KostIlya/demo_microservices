package ru.t1.demo_t1.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeLimitExceedLogDTO {
    @JsonProperty("method_signature")
    private String methodSignature;
    @JsonProperty("execution_time")
    private Long executionTime;
    @JsonProperty("date_time")
    private LocalDateTime dateTime;
    @JsonProperty("limit_time")
    private Long limitTime;
}
