package br.com.rdalloglio.scf.CategoryTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.rdalloglio.scf.dtos.CategoryRequestDTO;
import br.com.rdalloglio.scf.dtos.CategoryUpdateRequestDTO;
import br.com.rdalloglio.scf.enums.CategoryType;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CategoryIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String BASE_URL = "/api/categories";

    @Test
    @Order(1)
    void shouldCreateCategory() throws Exception {
        var dto = new CategoryRequestDTO("Transporte", CategoryType.DESPESA);

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Transporte"))
            .andExpect(jsonPath("$.type").value("DESPESA"));
    }

    @Test
    @Order(2)
    void shouldListAllCategories() throws Exception {
        mockMvc.perform(get(BASE_URL))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Order(3)
    void shouldGetCategoryById() throws Exception {
        mockMvc.perform(get(BASE_URL + "/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Alimentação"))
            .andExpect(jsonPath("$.type").value("DESPESA"));
    }

    @Test
    @Order(4)
    void shouldUpdateCategoryWithPatch() throws Exception {
        var patchDto = new CategoryUpdateRequestDTO("Mobilidade", null);

        mockMvc.perform(patch(BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patchDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Mobilidade"));
    }

    @Test
    @Order(5)
    void shouldDeleteCategory() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/1"))
            .andExpect(status().isNoContent());
    }

    @Test
    @Order(6)
    void shouldReturnBadRequestWhenCreatingCategoryWithInvalidData() throws Exception {
        var invalidDto = new CategoryRequestDTO("", null); // Nome vazio e tipo nulo

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Order(7)
    void shouldReturnNotFoundWhenGettingNonExistentCategory() throws Exception {
        mockMvc.perform(get(BASE_URL + "/999")) // ID inexistente
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("Categoria com ID 999 não encontrada."));
    }

    @Test
    @Order(8)
    void shouldReturnBadRequestWhenUpdatingCategoryWithInvalidData() throws Exception {
        var invalidDto = new CategoryRequestDTO("", null); // Nome vazio e tipo nulo

        mockMvc.perform(put(BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Order(9)
    void shouldReturnNotFoundWhenDeletingNonExistentCategory() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/999")) // ID inexistente
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("Categoria com ID 999 não encontrada."));
    }
}