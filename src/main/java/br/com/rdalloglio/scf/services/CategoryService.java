package br.com.rdalloglio.scf.services;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.rdalloglio.scf.dtos.CategoryRequestDTO;
import br.com.rdalloglio.scf.dtos.CategoryResponseDTO;
import br.com.rdalloglio.scf.entities.Category;
import br.com.rdalloglio.scf.enums.CategoryType;
import br.com.rdalloglio.scf.mappers.CategoryMapper;
import br.com.rdalloglio.scf.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    public CategoryResponseDTO create(CategoryRequestDTO dto) {
        Category category = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(category));
    }

    public List<CategoryResponseDTO> findAll(CategoryType type) {
        List<Category> list = (type != null) ? repository.findAllByType(type) : repository.findAll();
        return list.stream().map(mapper::toDTO).toList();
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}