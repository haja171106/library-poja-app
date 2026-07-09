package com.school.haja.endpoint.controller;

import com.school.haja.dto.GenreRevenueResponse;
import com.school.haja.entities.Library;
import com.school.haja.service.LibraryService;
import com.school.haja.service.SaleService;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/libraries")
@AllArgsConstructor
public class LibraryController {

  private final LibraryService libraryService;
  private final SaleService saleService;

  @GetMapping("/{libraryId}/revenue-by-genre")
  public List<GenreRevenueResponse> getRevenueByGenre(
      @PathVariable UUID libraryId,
      @RequestParam(required = false, name = "genre") List<String> genres) {
    return saleService.getRevenueByGenre(libraryId, genres);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Library create(@RequestBody Library library) {
    return libraryService.create(library);
  }

  @GetMapping
  public List<Library> getAll() {
    return libraryService.getAll();
  }

  @GetMapping("/{id}")
  public Library getById(@PathVariable UUID id) {
    return libraryService.getById(id);
  }

  @PutMapping("/{id}")
  public Library update(@PathVariable UUID id, @RequestBody Library library) {
    return libraryService.update(id, library);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable UUID id) {
    libraryService.delete(id);
  }
}
