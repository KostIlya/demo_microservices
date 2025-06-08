package ru.t1.demo_t1.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.t1.demo_t1.model.enums.AccountStatusEnum;
import ru.t1.demo_t1.model.enums.AccountTypeEnum;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class AccountRequestDTO {
    @JsonProperty("client_id")
    private UUID clientId;
    @JsonProperty("type_account")
    private AccountTypeEnum type;
    @JsonProperty("balance")
    private BigDecimal balance;
    @JsonProperty("status")
    private AccountStatusEnum status;
    @JsonProperty("account_id")
    private UUID accountId;
}