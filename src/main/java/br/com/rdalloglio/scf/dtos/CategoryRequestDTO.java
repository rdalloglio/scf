package br.com.rdalloglio.scf.dtos;

import static br.com.rdalloglio.scf.constants.CategoryMessageKeys.NAME_REQUIRED;
import static br.com.rdalloglio.scf.constants.CategoryMessageKeys.TYPE_REQUIRED;

import br.com.rdalloglio.scf.enums.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDTO {
    
    @NotBlank(message = NAME_REQUIRED)
    private String name;

    @NotNull(message = TYPE_REQUIRED)
    private CategoryType type;
}