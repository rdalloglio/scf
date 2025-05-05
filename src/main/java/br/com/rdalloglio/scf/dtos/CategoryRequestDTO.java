package br.com.rdalloglio.scf.dtos;

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
    
    @NotBlank
    private String name;

    @NotNull
    private CategoryType type;
}