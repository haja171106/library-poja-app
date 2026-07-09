package com.school.haja.endpoint.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.school.haja.dto.BookStockResponse;
import com.school.haja.dto.EditionStockResponse;
import com.school.haja.dto.StockResponse;
import com.school.haja.entities.BookFormat;
import com.school.haja.exception.GlobalExceptionHandler;
import com.school.haja.service.StockService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(StockController.class)
@Import(GlobalExceptionHandler.class)
class StockControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private StockService stockService;

  private static final UUID LIBRARY_ID = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");
  private static final UUID BOOK_ID = UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc");
  private static final UUID BOOK_EDITION_ID =
      UUID.fromString("dddddddd-dddd-dddd-dddd-dddddddddddd");

  @Test
  void getStockByFormat_shouldReturn200() throws Exception {
    StockResponse response = new StockResponse(LIBRARY_ID, BookFormat.PAPERBACK, 20, 8, 12);
    when(stockService.getStock(LIBRARY_ID, BookFormat.PAPERBACK)).thenReturn(response);

    mockMvc
        .perform(get("/api/libraries/{libraryId}/stocks", LIBRARY_ID).param("format", "PAPERBACK"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.libraryId").value(LIBRARY_ID.toString()))
        .andExpect(jsonPath("$.format").value("PAPERBACK"))
        .andExpect(jsonPath("$.totalArrivals").value(20))
        .andExpect(jsonPath("$.totalSales").value(8))
        .andExpect(jsonPath("$.stock").value(12));
  }

  @Test
  void getStockByFormat_withInvalidFormat_shouldReturn400() throws Exception {
    mockMvc
        .perform(
            get("/api/libraries/{libraryId}/stocks", LIBRARY_ID).param("format", "NOT_A_FORMAT"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void getAllStocks_shouldReturn200WithAllFormats() throws Exception {
    List<StockResponse> responses =
        List.of(
            new StockResponse(LIBRARY_ID, BookFormat.PAPERBACK, 20, 8, 12),
            new StockResponse(LIBRARY_ID, BookFormat.HARDCOVER, 5, 2, 3),
            new StockResponse(LIBRARY_ID, BookFormat.SOFTCOVER, 0, 0, 0),
            new StockResponse(LIBRARY_ID, BookFormat.LARGE_PRINT, 10, 15, -5));

    when(stockService.getAllStocks(LIBRARY_ID)).thenReturn(responses);

    mockMvc
        .perform(get("/api/libraries/{libraryId}/stocks", LIBRARY_ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(4)))
        .andExpect(jsonPath("$[3].stock").value(-5));
  }

  @Test
  void getStockByBook_shouldReturn200() throws Exception {
    BookStockResponse response = new BookStockResponse(LIBRARY_ID, BOOK_ID, 50, 20, 30);
    when(stockService.getStockByBook(LIBRARY_ID, BOOK_ID)).thenReturn(response);

    mockMvc
        .perform(get("/api/libraries/{libraryId}/stocks/books/{bookId}", LIBRARY_ID, BOOK_ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.libraryId").value(LIBRARY_ID.toString()))
        .andExpect(jsonPath("$.bookId").value(BOOK_ID.toString()))
        .andExpect(jsonPath("$.totalArrivals").value(50))
        .andExpect(jsonPath("$.totalSales").value(20))
        .andExpect(jsonPath("$.stock").value(30));
  }

  @Test
  void getStockByBook_withNoArrivalsOrSales_shouldReturnZeroStock() throws Exception {
    BookStockResponse response = new BookStockResponse(LIBRARY_ID, BOOK_ID, 0, 0, 0);
    when(stockService.getStockByBook(LIBRARY_ID, BOOK_ID)).thenReturn(response);

    mockMvc
        .perform(get("/api/libraries/{libraryId}/stocks/books/{bookId}", LIBRARY_ID, BOOK_ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.stock").value(0));
  }

  @Test
  void getStockByEdition_shouldReturn200() throws Exception {
    EditionStockResponse response =
        new EditionStockResponse(LIBRARY_ID, BOOK_EDITION_ID, 15, 5, 10);
    when(stockService.getStockByEdition(LIBRARY_ID, BOOK_EDITION_ID)).thenReturn(response);

    mockMvc
        .perform(
            get(
                "/api/libraries/{libraryId}/stocks/editions/{bookEditionId}",
                LIBRARY_ID,
                BOOK_EDITION_ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.libraryId").value(LIBRARY_ID.toString()))
        .andExpect(jsonPath("$.bookEditionId").value(BOOK_EDITION_ID.toString()))
        .andExpect(jsonPath("$.totalArrivals").value(15))
        .andExpect(jsonPath("$.totalSales").value(5))
        .andExpect(jsonPath("$.stock").value(10));
  }

  @Test
  void getStockByEdition_withMoreSalesThanArrivals_shouldReturnNegativeStock() throws Exception {
    EditionStockResponse response = new EditionStockResponse(LIBRARY_ID, BOOK_EDITION_ID, 5, 8, -3);
    when(stockService.getStockByEdition(LIBRARY_ID, BOOK_EDITION_ID)).thenReturn(response);

    mockMvc
        .perform(
            get(
                "/api/libraries/{libraryId}/stocks/editions/{bookEditionId}",
                LIBRARY_ID,
                BOOK_EDITION_ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.stock").value(-3));
  }
}
