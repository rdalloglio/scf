package br.com.rdalloglio.scf.mappers;

import org.springframework.stereotype.Component;

import br.com.rdalloglio.scf.dtos.CategoryRequestDTO;
import br.com.rdalloglio.scf.dtos.CategoryResponseDTO;
import br.com.rdalloglio.scf.entities.Category;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryRequestDTO dto) {
        return Category.builder()
                .name(dto.getName())
                .type(dto.getType())
                .build();
    }

    public CategoryResponseDTO toDTO(Category entity) {
        return CategoryResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .type(entity.getType())
                .build();
    }
}
