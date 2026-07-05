package com.school.haja.endpoint.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.haja.entities.Book;
import com.school.haja.exception.GlobalExceptionHandler;
import com.school.haja.service.BookService;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BookController.class)
@Import(GlobalExceptionHandler.class)
class BookControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private BookService bookService;

  private static final UUID BOOK_ID = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

  @Test
  void create_shouldReturn201() throws Exception {
    Book request =
        new Book(
            null, "The Hobbit", "9780547928227", 310, 24.99, Instant.parse("1937-09-21T00:00:00Z"));
    Book response =
        new Book(
            BOOK_ID,
            "The Hobbit",
            "9780547928227",
            310,
            24.99,
            Instant.parse("1937-09-21T00:00:00Z"));

    when(bookService.create(any(Book.class))).thenReturn(response);

    mockMvc
        .perform(
            post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(BOOK_ID.toString()))
        .andExpect(jsonPath("$.title").value("The Hobbit"));
  }

  @Test
  void getById_shouldReturn200() throws Exception {
    Book book =
        new Book(
            BOOK_ID,
            "The Hobbit",
            "9780547928227",
            310,
            24.99,
            Instant.parse("1937-09-21T00:00:00Z"));
    when(bookService.getById(BOOK_ID)).thenReturn(book);

    mockMvc
        .perform(get("/books/{id}", BOOK_ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("The Hobbit"));
  }

  @Test
  void getById_whenNotFound_shouldReturn404() throws Exception {
    when(bookService.getById(BOOK_ID))
        .thenThrow(new EntityNotFoundException("Book not found: " + BOOK_ID));

    mockMvc
        .perform(get("/books/{id}", BOOK_ID))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.message").value("Book not found: " + BOOK_ID));
  }

  @Test
  void getAll_shouldReturn200() throws Exception {
    Book book =
        new Book(
            BOOK_ID,
            "The Hobbit",
            "9780547928227",
            310,
            24.99,
            Instant.parse("1937-09-21T00:00:00Z"));
    when(bookService.getAll()).thenReturn(List.of(book));

    mockMvc
        .perform(get("/books"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].title").value("The Hobbit"));
  }

  @Test
  void update_shouldReturn200() throws Exception {
    Book request =
        new Book(
            null,
            "Updated title",
            "9780547928227",
            310,
            29.99,
            Instant.parse("1937-09-21T00:00:00Z"));
    Book response =
        new Book(
            BOOK_ID,
            "Updated title",
            "9780547928227",
            310,
            29.99,
            Instant.parse("1937-09-21T00:00:00Z"));

    when(bookService.update(eq(BOOK_ID), any(Book.class))).thenReturn(response);

    mockMvc
        .perform(
            put("/books/{id}", BOOK_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Updated title"));
  }

  @Test
  void delete_shouldReturn204() throws Exception {
    mockMvc.perform(delete("/books/{id}", BOOK_ID)).andExpect(status().isNoContent());

    verify(bookService).delete(BOOK_ID);
  }
}
