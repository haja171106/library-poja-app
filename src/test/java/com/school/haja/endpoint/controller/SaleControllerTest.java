package com.school.haja.endpoint.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
import com.school.haja.entities.BookFormat;
import com.school.haja.entities.Sale;
import com.school.haja.entities.SaleStatus;
import com.school.haja.exception.GlobalExceptionHandler;
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

@WebMvcTest(SaleController.class)
@Import(GlobalExceptionHandler.class)
class SaleControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private SaleService saleService;

  private static final UUID SALE_ID = UUID.fromString("eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee");
  private static final UUID LIBRARY_ID = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");
  private static final UUID BOOK_EDITION_ID =
      UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff");

  private Sale sampleSale(UUID id) {
    return new Sale(
        id, 24.99, 2, SaleStatus.PENDING, BookFormat.PAPERBACK, LIBRARY_ID, BOOK_EDITION_ID);
  }

  @Test
  void create_shouldReturn201() throws Exception {
    Sale request = sampleSale(null);
    Sale response = sampleSale(SALE_ID);

    when(saleService.create(any(Sale.class))).thenReturn(response);

    mockMvc
        .perform(
            post("/sales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(SALE_ID.toString()))
        .andExpect(jsonPath("$.status").value("PENDING"));
  }

  @Test
  void create_whenLibraryNotFound_shouldReturn404() throws Exception {
    Sale request = sampleSale(null);

    when(saleService.create(any(Sale.class)))
        .thenThrow(new EntityNotFoundException("Library not found: " + LIBRARY_ID));

    mockMvc
        .perform(
            post("/sales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Library not found: " + LIBRARY_ID));
  }

  @Test
  void getAll_shouldReturn200() throws Exception {
    when(saleService.getAll()).thenReturn(List.of(sampleSale(SALE_ID)));

    mockMvc
        .perform(get("/sales"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(SALE_ID.toString()));
  }

  @Test
  void getById_shouldReturn200() throws Exception {
    when(saleService.getById(SALE_ID)).thenReturn(sampleSale(SALE_ID));

    mockMvc
        .perform(get("/sales/{id}", SALE_ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.price").value(24.99));
  }

  @Test
  void getById_whenNotFound_shouldReturn404() throws Exception {
    when(saleService.getById(SALE_ID))
        .thenThrow(new EntityNotFoundException("Sale not found: " + SALE_ID));

    mockMvc
        .perform(get("/sales/{id}", SALE_ID))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Sale not found: " + SALE_ID));
  }

  @Test
  void getById_withInvalidUUID_shouldReturn400() throws Exception {
    mockMvc.perform(get("/sales/{id}", "not-a-valid-uuid")).andExpect(status().isBadRequest());
  }

  @Test
  void update_shouldReturn200() throws Exception {
    Sale request = sampleSale(null);
    request.setStatus(SaleStatus.DONE);
    Sale response = sampleSale(SALE_ID);
    response.setStatus(SaleStatus.DONE);

    when(saleService.update(eq(SALE_ID), any(Sale.class))).thenReturn(response);

    mockMvc
        .perform(
            put("/sales/{id}", SALE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("DONE"));
  }

  @Test
  void update_whenNotFound_shouldReturn404() throws Exception {
    Sale request = sampleSale(null);

    when(saleService.update(eq(SALE_ID), any(Sale.class)))
        .thenThrow(new EntityNotFoundException("Sale not found: " + SALE_ID));

    mockMvc
        .perform(
            put("/sales/{id}", SALE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Sale not found: " + SALE_ID));
  }

  @Test
  void delete_shouldReturn204() throws Exception {
    mockMvc.perform(delete("/sales/{id}", SALE_ID)).andExpect(status().isNoContent());

    verify(saleService).delete(SALE_ID);
  }

  @Test
  void delete_whenNotFound_shouldReturn404() throws Exception {
    doThrow(new EntityNotFoundException("Sale not found: " + SALE_ID))
        .when(saleService)
        .delete(SALE_ID);

    mockMvc
        .perform(delete("/sales/{id}", SALE_ID))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Sale not found: " + SALE_ID));
  }
}
