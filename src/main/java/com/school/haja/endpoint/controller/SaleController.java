package com.school.haja.endpoint.controller;

import com.school.haja.dto.GenreRevenueResponse;
import com.school.haja.entities.Sale;
import com.school.haja.service.SaleService;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sales")
@AllArgsConstructor
public class SaleController {

  private final SaleService saleService;

  @GetMapping("/libraries/{libraryId}/revenue-by-genre")
  public List<GenreRevenueResponse> getRevenueByGenre(@PathVariable UUID libraryId) {
    return saleService.getRevenueByGenre(libraryId);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Sale create(@RequestBody Sale sale) {
    return saleService.create(sale);
  }

  @GetMapping
  public List<Sale> getAll() {
    return saleService.getAll();
  }

  @GetMapping("/{id}")
  public Sale getById(@PathVariable UUID id) {
    return saleService.getById(id);
  }

  @PutMapping("/{id}")
  public Sale update(@PathVariable UUID id, @RequestBody Sale sale) {
    return saleService.update(id, sale);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable UUID id) {
    saleService.delete(id);
  }
}
