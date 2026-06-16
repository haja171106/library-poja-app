package com.school.haja.endpoint.controller;

import com.school.haja.entities.Book;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BookController {

  @GetMapping
  public List<Book> getAllBooks() {
    return null;
  }

  @GetMapping("/{id}")
  public Book getBookById(@PathVariable UUID id) {
    return null;
  }

  @PostMapping
  public Book createBook(@RequestBody Book book) {
    return null;
  }

  @PutMapping("/{id}")
  public Book updateBook(@PathVariable UUID id, @RequestBody Book book) {
    return null;
  }

  @DeleteMapping("/{id}")
  public void deleteBook(@PathVariable UUID id) {}
}
