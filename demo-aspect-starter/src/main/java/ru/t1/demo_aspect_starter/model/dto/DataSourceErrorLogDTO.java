package ru.t1.demo_aspect_starter.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class DataSourceErrorLogDTO {
    @JsonProperty("stacktrace")
    private String stacktrace;
    @JsonProperty("message")
    private String message;
    @JsonProperty("method_signature")
    private String methodSignature;
}
