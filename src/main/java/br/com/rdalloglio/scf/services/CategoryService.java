package br.com.rdalloglio.scf.services;

import static br.com.rdalloglio.scf.constants.CategoryMessageKeys.NOT_FOUND;

import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import br.com.rdalloglio.scf.dtos.CategoryRequestDTO;
import br.com.rdalloglio.scf.dtos.CategoryResponseDTO;
import br.com.rdalloglio.scf.dtos.CategoryUpdateRequestDTO;
import br.com.rdalloglio.scf.entities.Category;
import br.com.rdalloglio.scf.enums.CategoryType;
import br.com.rdalloglio.scf.exceptions.CategoryNotFoundException;
import br.com.rdalloglio.scf.mappers.CategoryMapper;
import br.com.rdalloglio.scf.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;
    private final CategoryMapper mapper;
    private final MessageSource messageSource;

    public CategoryResponseDTO create(CategoryRequestDTO dto) {
        Category category = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(category));
    }

    public CategoryResponseDTO findById(Long id) {
        Category category = getCategoryById(id);
        return mapper.toDTO(category);
    }
    
    public List<CategoryResponseDTO> findAll(CategoryType type) {
        List<Category> list = (type != null) ? repository.findAllByType(type) : repository.findAll();
        return list.stream().map(mapper::toDTO).toList();
    }

    public CategoryResponseDTO update(Long id, CategoryRequestDTO dto) {
        Category category = getCategoryById(id);
    
        category.setName(dto.getName());
        category.setType(dto.getType());

        return mapper.toDTO(repository.save(category));
    }

    public CategoryResponseDTO patch(Long id, CategoryUpdateRequestDTO dto) {
        Category category = getCategoryById(id);
    
        if (dto.name() != null) {
            category.setName(dto.name());
        }
        if (dto.type() != null) {
            category.setType(dto.type());
        }
    
        return mapper.toDTO(repository.save(category));
    }    

    public void delete(Long id) {
        Category category = getCategoryById(id);
        repository.delete(category);
    }


    private String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, Locale.getDefault());
    }

    private Category getCategoryById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(
                    getMessage(NOT_FOUND, id)
                ));
    }
}