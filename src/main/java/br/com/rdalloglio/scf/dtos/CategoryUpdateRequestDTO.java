package br.com.rdalloglio.scf.dtos;

import br.com.rdalloglio.scf.enums.CategoryType;

public record CategoryUpdateRequestDTO(
    String name,
    CategoryType type
) {}