package br.com.rdalloglio.scf.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AccountRequest(
    @NotBlank(message = "O nome da conta é obrigatório")
    String name,

    @NotNull(message = "O saldo inicial é obrigatório")
    @DecimalMin(value = "0.0", inclusive = true, message = "O saldo deve ser maior ou igual a zero")
    BigDecimal balance
) {}