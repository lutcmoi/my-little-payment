package org.bdabos.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@Data
@Validated
@NoArgsConstructor
public class OwnerToAccountTransferDto {
    @NotNull
    private String currency;

    private Long accountTo;
    private Long ownerTo;

    @NotNull
    private BigDecimal amount;
}
