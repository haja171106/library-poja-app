package com.school.haja.endpoint.controller;

import com.school.haja.dto.StockResponse;
import com.school.haja.entities.BookFormat;
import com.school.haja.service.StockService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/libraries/{libraryId}/stocks")
@RequiredArgsConstructor
public class StockController {

  private final StockService stockService;

  @GetMapping
  public ResponseEntity<List<StockResponse>> getAllStocks(@PathVariable UUID libraryId) {
    return ResponseEntity.ok(stockService.getAllStocks(libraryId));
  }

  @GetMapping(params = "format")
  public ResponseEntity<StockResponse> getStockByFormat(
      @PathVariable UUID libraryId, @RequestParam BookFormat format) {
    return ResponseEntity.ok(stockService.getStock(libraryId, format));
  }
}
