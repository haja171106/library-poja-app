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
import com.school.haja.entities.Arrival;
import com.school.haja.entities.BookFormat;
import com.school.haja.exception.GlobalExceptionHandler;
import com.school.haja.service.ArrivalService;
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

@WebMvcTest(ArrivalController.class)
@Import(GlobalExceptionHandler.class)
class ArrivalControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private ArrivalService arrivalService;

  private static final UUID ARRIVAL_ID = UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc");
  private static final UUID LIBRARY_ID = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");

  private Arrival sampleArrival(UUID id) {
    return new Arrival(id, 20, BookFormat.PAPERBACK, LIBRARY_ID);
  }

  @Test
  void create_shouldReturn201() throws Exception {
    Arrival request = sampleArrival(null);
    Arrival response = sampleArrival(ARRIVAL_ID);

    when(arrivalService.create(any(Arrival.class))).thenReturn(response);

    mockMvc
        .perform(
            post("/arrivals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(ARRIVAL_ID.toString()))
        .andExpect(jsonPath("$.nbr_book").value(20))
        .andExpect(jsonPath("$.format").value("PAPERBACK"));
  }

  @Test
  void create_whenLibraryNotFound_shouldReturn404() throws Exception {
    Arrival request = sampleArrival(null);

    when(arrivalService.create(any(Arrival.class)))
        .thenThrow(new EntityNotFoundException("Library not found: " + LIBRARY_ID));

    mockMvc
        .perform(
            post("/arrivals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Library not found: " + LIBRARY_ID));
  }

  @Test
  void getAll_shouldReturn200() throws Exception {
    when(arrivalService.getAll()).thenReturn(List.of(sampleArrival(ARRIVAL_ID)));

    mockMvc
        .perform(get("/arrivals"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(ARRIVAL_ID.toString()));
  }

  @Test
  void getById_shouldReturn200() throws Exception {
    when(arrivalService.getById(ARRIVAL_ID)).thenReturn(sampleArrival(ARRIVAL_ID));

    mockMvc
        .perform(get("/arrivals/{id}", ARRIVAL_ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nbr_book").value(20));
  }

  @Test
  void getById_whenNotFound_shouldReturn404() throws Exception {
    when(arrivalService.getById(ARRIVAL_ID))
        .thenThrow(new EntityNotFoundException("Arrival not found: " + ARRIVAL_ID));

    mockMvc
        .perform(get("/arrivals/{id}", ARRIVAL_ID))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Arrival not found: " + ARRIVAL_ID));
  }

  @Test
  void getById_withInvalidUUID_shouldReturn400() throws Exception {
    mockMvc.perform(get("/arrivals/{id}", "not-a-valid-uuid")).andExpect(status().isBadRequest());
  }

  @Test
  void update_shouldReturn200() throws Exception {
    Arrival request = sampleArrival(null);
    request.setNbr_book(50);
    Arrival response = sampleArrival(ARRIVAL_ID);
    response.setNbr_book(50);

    when(arrivalService.update(eq(ARRIVAL_ID), any(Arrival.class))).thenReturn(response);

    mockMvc
        .perform(
            put("/arrivals/{id}", ARRIVAL_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nbr_book").value(50));
  }

  @Test
  void update_whenNotFound_shouldReturn404() throws Exception {
    Arrival request = sampleArrival(null);

    when(arrivalService.update(eq(ARRIVAL_ID), any(Arrival.class)))
        .thenThrow(new EntityNotFoundException("Arrival not found: " + ARRIVAL_ID));

    mockMvc
        .perform(
            put("/arrivals/{id}", ARRIVAL_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Arrival not found: " + ARRIVAL_ID));
  }

  @Test
  void delete_shouldReturn204() throws Exception {
    mockMvc.perform(delete("/arrivals/{id}", ARRIVAL_ID)).andExpect(status().isNoContent());

    verify(arrivalService).delete(ARRIVAL_ID);
  }

  @Test
  void delete_whenNotFound_shouldReturn404() throws Exception {
    doThrow(new EntityNotFoundException("Arrival not found: " + ARRIVAL_ID))
        .when(arrivalService)
        .delete(ARRIVAL_ID);

    mockMvc
        .perform(delete("/arrivals/{id}", ARRIVAL_ID))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Arrival not found: " + ARRIVAL_ID));
  }
}
