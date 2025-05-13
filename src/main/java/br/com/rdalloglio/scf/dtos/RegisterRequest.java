package br.com.rdalloglio.scf.dtos;

import br.com.rdalloglio.scf.enums.Role;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
    @NotBlank String username,
    @NotBlank String password,
    @NotBlank Role role
) {}