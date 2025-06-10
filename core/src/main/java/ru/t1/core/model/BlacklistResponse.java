package ru.t1.core.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.t1.core.model.enums.StatusClientEnum;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class BlacklistResponse {
    @JsonProperty("client_id")
    public UUID clientId;
    @JsonProperty("client_status")
    public StatusClientEnum clientStatus;
}
