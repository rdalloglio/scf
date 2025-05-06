package br.com.rdalloglio.scf.CategoryTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import br.com.rdalloglio.scf.dtos.CategoryRequestDTO;
import br.com.rdalloglio.scf.dtos.CategoryResponseDTO;
import br.com.rdalloglio.scf.dtos.CategoryUpdateRequestDTO;
import br.com.rdalloglio.scf.entities.Category;
import br.com.rdalloglio.scf.enums.CategoryType;
import br.com.rdalloglio.scf.exceptions.CategoryNotFoundException;
import br.com.rdalloglio.scf.mappers.CategoryMapper;
import br.com.rdalloglio.scf.repositories.CategoryRepository;
import br.com.rdalloglio.scf.services.CategoryService;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository repository;

    @Mock
    private CategoryMapper mapper;
    
    @Mock
    private MessageSource messageSource;
    
    @InjectMocks
    private CategoryService service;

    @Test
    void shouldReturnCategoryWhenIdExists() {
        Category categoria = new Category(1L, "Alimentação", CategoryType.DESPESA);
        
        when(mapper.toDTO(any(Category.class)))
        .thenReturn(new CategoryResponseDTO(categoria.getId(), categoria.getName(), categoria.getType()));
        when(repository.findById(1L)).thenReturn(Optional.of(categoria));
        
        CategoryResponseDTO dto = service.findById(1L);

        assertEquals("Alimentação", dto.getName());
        assertEquals(CategoryType.DESPESA, dto.getType());
    }

    @Test
    void shouldThrowExceptionWhenIdDoesNotExist() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> service.findById(99L));
    }

    @Test
    void shouldSaveNewCategory() {
        CategoryRequestDTO dto = new CategoryRequestDTO("Saúde", CategoryType.DESPESA);
        Category categoriaSalva = new Category(10L, "Saúde", CategoryType.DESPESA);

        when(mapper.toDTO(any(Category.class)))
        .thenReturn(new CategoryResponseDTO(categoriaSalva.getId(), categoriaSalva.getName(), categoriaSalva.getType()));
        when(repository.save(any())).thenReturn(categoriaSalva);

        CategoryResponseDTO resultado = service.create(dto);

        assertEquals("Saúde", resultado.getName());
        assertEquals(CategoryType.DESPESA, resultado.getType());
    }

    @Test
    void shouldUpdateCategoryWhenIdExists() {
        Long id = 1L;
        Category existente = new Category(id, "Antigo", CategoryType.RECEITA);
        CategoryRequestDTO novoDto = new CategoryRequestDTO("Novo Nome", CategoryType.DESPESA);

        when(repository.findById(id)).thenReturn(Optional.of(existente));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(mapper.toDTO(any(Category.class)))
        .thenReturn(new CategoryResponseDTO(id, novoDto.getName(), novoDto.getType()));
        
        CategoryResponseDTO atualizado = service.update(id, novoDto);
        
        assertEquals("Novo Nome", atualizado.getName());
        assertEquals(CategoryType.DESPESA, atualizado.getType());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonexistentCategory() {
        Long id = 42L;
        CategoryRequestDTO dto = new CategoryRequestDTO("Teste", CategoryType.RECEITA);
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> service.update(id, dto));
    }

    @Test
    void shouldUpdateOnlyName() {
        Long id = 1L;
        Category existente = new Category(id, "Antigo", CategoryType.RECEITA);
        CategoryUpdateRequestDTO patchDTO = new CategoryUpdateRequestDTO("Novo Nome", null);
        Category atualizado = new Category(id, "Novo Nome", CategoryType.RECEITA);
        CategoryResponseDTO resultadoDTO = new CategoryResponseDTO(id, "Novo Nome", CategoryType.RECEITA);

        when(repository.findById(id)).thenReturn(Optional.of(existente));
        when(repository.save(any(Category.class))).thenReturn(atualizado);
        when(mapper.toDTO(any(Category.class))).thenReturn(resultadoDTO);

        CategoryResponseDTO resultado = service.patch(id, patchDTO);

        assertEquals("Novo Nome", resultado.getName());
        assertEquals(CategoryType.RECEITA, resultado.getType());
    }

    @Test
    void shouldUpdateOnlyType() {
        Long id = 2L;
        Category existente = new Category(id, "Outros", CategoryType.RECEITA);
        CategoryUpdateRequestDTO patchDTO = new CategoryUpdateRequestDTO(null, CategoryType.DESPESA);
        Category atualizado = new Category(id, "Outros", CategoryType.DESPESA);
        CategoryResponseDTO resultadoDTO = new CategoryResponseDTO(id, "Outros", CategoryType.DESPESA);

        when(repository.findById(id)).thenReturn(Optional.of(existente));
        when(repository.save(any(Category.class))).thenReturn(atualizado);
        when(mapper.toDTO(any(Category.class))).thenReturn(resultadoDTO);

        CategoryResponseDTO resultado = service.patch(id, patchDTO);

        assertEquals("Outros", resultado.getName());
        assertEquals(CategoryType.DESPESA, resultado.getType());
    }

    @Test
    void shouldUpdateBothFields() {
        Long id = 3L;
        Category existente = new Category(id, "Velho", CategoryType.RECEITA);
        CategoryUpdateRequestDTO patchDTO = new CategoryUpdateRequestDTO("Novo", CategoryType.DESPESA);
        Category atualizado = new Category(id, "Novo", CategoryType.DESPESA);
        CategoryResponseDTO resultadoDTO = new CategoryResponseDTO(id, "Novo", CategoryType.DESPESA);

        when(repository.findById(id)).thenReturn(Optional.of(existente));
        when(repository.save(any(Category.class))).thenReturn(atualizado);
        when(mapper.toDTO(any(Category.class))).thenReturn(resultadoDTO);

        CategoryResponseDTO resultado = service.patch(id, patchDTO);

        assertEquals("Novo", resultado.getName());
        assertEquals(CategoryType.DESPESA, resultado.getType());
    }

    @Test
    void shouldThrowExceptionWhenCategoryNotFound() {
        Long id = 99L;
        CategoryUpdateRequestDTO patchDTO = new CategoryUpdateRequestDTO("Teste", CategoryType.RECEITA);

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> service.patch(id, patchDTO));
    }

    @Test
    void shouldDeleteCategoryWhenIdExists() {
        Long id = 2L;
        Category categoria = new Category(id, "Excluir", CategoryType.DESPESA);
        when(repository.findById(id)).thenReturn(Optional.of(categoria));
        doNothing().when(repository).delete(categoria);

        assertDoesNotThrow(() -> service.delete(id));
        verify(repository).delete(any(Category.class));
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonexistentCategory() {
        Long id = 100L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> service.delete(id));
    }
}