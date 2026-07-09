package com.school.haja.endpoint.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.haja.dto.GenreRevenueResponse;
import com.school.haja.entities.Library;
import com.school.haja.exception.GlobalExceptionHandler;
import com.school.haja.service.LibraryService;
import com.school.haja.service.SaleService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(LibraryController.class)
@Import(GlobalExceptionHandler.class)
class LibraryControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private LibraryService libraryService;

  @MockBean private SaleService saleService;

  private static final UUID LIBRARY_ID = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");
  private static final UUID GENRE_ID = UUID.fromString("dddddddd-dddd-dddd-dddd-dddddddddddd");

  private Library sampleLibrary(UUID id) {
    return new Library(id, "Bibliothèque centrale");
  }

  @Test
  void create_shouldReturn201() throws Exception {
    Library request = sampleLibrary(null);
    Library response = sampleLibrary(LIBRARY_ID);

    when(libraryService.create(any(Library.class))).thenReturn(response);

    mockMvc
        .perform(
            post("/libraries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(LIBRARY_ID.toString()))
        .andExpect(jsonPath("$.name").value("Bibliothèque centrale"));
  }

  @Test
  void getAll_shouldReturn200() throws Exception {
    when(libraryService.getAll()).thenReturn(List.of(sampleLibrary(LIBRARY_ID)));

    mockMvc
        .perform(get("/libraries"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(LIBRARY_ID.toString()));
  }

  @Test
  void getById_shouldReturn200() throws Exception {
    when(libraryService.getById(LIBRARY_ID)).thenReturn(sampleLibrary(LIBRARY_ID));

    mockMvc
        .perform(get("/libraries/{id}", LIBRARY_ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Bibliothèque centrale"));
  }

  @Test
  void getById_whenNotFound_shouldReturn404() throws Exception {
    when(libraryService.getById(LIBRARY_ID))
        .thenThrow(new EntityNotFoundException("Library not found: " + LIBRARY_ID));

    mockMvc
        .perform(get("/libraries/{id}", LIBRARY_ID))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Library not found: " + LIBRARY_ID));
  }

  @Test
  void getById_withInvalidUUID_shouldReturn400() throws Exception {
    mockMvc.perform(get("/libraries/{id}", "not-a-valid-uuid")).andExpect(status().isBadRequest());
  }

  @Test
  void update_shouldReturn200() throws Exception {
    Library request = sampleLibrary(null);
    request.setName("Nouvelle bibliothèque");
    Library response = sampleLibrary(LIBRARY_ID);
    response.setName("Nouvelle bibliothèque");

    when(libraryService.update(eq(LIBRARY_ID), any(Library.class))).thenReturn(response);

    mockMvc
        .perform(
            put("/libraries/{id}", LIBRARY_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Nouvelle bibliothèque"));
  }

  @Test
  void update_whenNotFound_shouldReturn404() throws Exception {
    Library request = sampleLibrary(null);

    when(libraryService.update(eq(LIBRARY_ID), any(Library.class)))
        .thenThrow(new EntityNotFoundException("Library not found: " + LIBRARY_ID));

    mockMvc
        .perform(
            put("/libraries/{id}", LIBRARY_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Library not found: " + LIBRARY_ID));
  }

  @Test
  void delete_shouldReturn204() throws Exception {
    mockMvc.perform(delete("/libraries/{id}", LIBRARY_ID)).andExpect(status().isNoContent());

    verify(libraryService).delete(LIBRARY_ID);
  }

  @Test
  void delete_whenNotFound_shouldReturn404() throws Exception {
    doThrow(new EntityNotFoundException("Library not found: " + LIBRARY_ID))
        .when(libraryService)
        .delete(LIBRARY_ID);

    mockMvc
        .perform(delete("/libraries/{id}", LIBRARY_ID))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Library not found: " + LIBRARY_ID));
  }

  @Test
  void getRevenueByGenre_withoutGenreFilter_shouldReturn200() throws Exception {
    List<GenreRevenueResponse> revenues =
        List.of(new GenreRevenueResponse(GENRE_ID, "Fantasy", 150.5));

    when(saleService.getRevenueByGenre(eq(LIBRARY_ID), isNull())).thenReturn(revenues);

    mockMvc
        .perform(get("/libraries/{libraryId}/revenue-by-genre", LIBRARY_ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].genreType").value("Fantasy"))
        .andExpect(jsonPath("$[0].revenue").value(150.5));
  }

  @Test
  void getRevenueByGenre_withGenreFilter_shouldReturn200() throws Exception {
    List<GenreRevenueResponse> revenues =
        List.of(new GenreRevenueResponse(GENRE_ID, "Fantasy", 150.5));

    when(saleService.getRevenueByGenre(eq(LIBRARY_ID), anyList())).thenReturn(revenues);

    mockMvc
        .perform(
            get("/libraries/{libraryId}/revenue-by-genre", LIBRARY_ID)
                .param("genre", "Fantasy", "SciFi"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].genreType").value("Fantasy"));
  }

  @Test
  void getRevenueByGenre_withInvalidLibraryId_shouldReturn400() throws Exception {
    mockMvc
        .perform(get("/libraries/{libraryId}/revenue-by-genre", "not-a-valid-uuid"))
        .andExpect(status().isBadRequest());
  }
}
