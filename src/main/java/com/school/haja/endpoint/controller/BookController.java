package com.school.haja.endpoint.controller;

import com.school.haja.entities.Book;
import com.school.haja.service.BookService;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
@AllArgsConstructor
public class BookController {

  private final BookService bookService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Book create(@RequestBody Book book) {
    return bookService.create(book);
  }

  @GetMapping
  public List<Book> getAll() {
    return bookService.getAll();
  }

  @GetMapping("/{id}")
  public Book getById(@PathVariable UUID id) {
    return bookService.getById(id);
  }

  @PutMapping("/{id}")
  public Book update(@PathVariable UUID id, @RequestBody Book book) {
    return bookService.update(id, book);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable UUID id) {
    bookService.delete(id);
  }
}
