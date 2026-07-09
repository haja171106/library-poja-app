package com.school.haja.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.school.haja.entities.Book;
import com.school.haja.repository.BookRepository;
import com.school.haja.repository.model.JBook;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

  @Mock private BookRepository bookRepository;

  @InjectMocks private BookService bookService;

  private JBook jBook(UUID id, String title, String isbn, int pages, double price, Instant date) {
    JBook book = new JBook();
    book.setId(id);
    book.setTitle(title);
    book.setIsbn(isbn);
    book.setNbr_page(pages);
    book.setPrice(price);
    book.setRelease_date(date);
    return book;
  }

  @Test
  void create_shouldSaveAndReturnBook() {
    UUID id = UUID.randomUUID();
    Instant date = Instant.parse("2020-01-01T00:00:00Z");
    Book book = new Book(id, "Les Misérables", "1234567890123", 500, 25.5, date);

    when(bookRepository.save(any(JBook.class)))
        .thenReturn(jBook(id, "Les Misérables", "1234567890123", 500, 25.5, date));

    Book result = bookService.create(book);

    assertThat(result.getId()).isEqualTo(id);
    assertThat(result.getTitle()).isEqualTo("Les Misérables");
    assertThat(result.getIsbn()).isEqualTo("1234567890123");
    assertThat(result.getNbr_page()).isEqualTo(500);
    assertThat(result.getPrice()).isEqualTo(25.5);
    assertThat(result.getRelease_date()).isEqualTo(date);
  }

  @Test
  void getById_whenExists_shouldReturnBook() {
    UUID id = UUID.randomUUID();
    when(bookRepository.findById(id))
        .thenReturn(Optional.of(jBook(id, "Title", "ISBN", 100, 10.0, Instant.now())));

    Book result = bookService.getById(id);

    assertThat(result.getTitle()).isEqualTo("Title");
  }

  @Test
  void getById_whenMissing_shouldThrow() {
    UUID id = UUID.randomUUID();
    when(bookRepository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> bookService.getById(id)).isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  void getAll_shouldReturnAllBooks() {
    when(bookRepository.findAll())
        .thenReturn(
            List.of(
                jBook(UUID.randomUUID(), "A", "1", 1, 1.0, Instant.now()),
                jBook(UUID.randomUUID(), "B", "2", 2, 2.0, Instant.now())));

    List<Book> result = bookService.getAll();

    assertThat(result).hasSize(2);
  }

  @Test
  void update_whenExists_shouldUpdateAndReturnBook() {
    UUID id = UUID.randomUUID();
    Instant newDate = Instant.parse("2021-06-01T00:00:00Z");
    JBook existing = jBook(id, "Old", "OldIsbn", 1, 1.0, Instant.now());
    Book update = new Book(id, "New", "NewIsbn", 300, 30.0, newDate);

    when(bookRepository.findById(id)).thenReturn(Optional.of(existing));
    when(bookRepository.save(existing)).thenReturn(existing);

    Book result = bookService.update(id, update);

    assertThat(result.getTitle()).isEqualTo("New");
    assertThat(result.getIsbn()).isEqualTo("NewIsbn");
    assertThat(result.getNbr_page()).isEqualTo(300);
    assertThat(result.getPrice()).isEqualTo(30.0);
    assertThat(result.getRelease_date()).isEqualTo(newDate);
  }

  @Test
  void update_whenMissing_shouldThrow() {
    UUID id = UUID.randomUUID();
    when(bookRepository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> bookService.update(id, new Book(id, "X", "Y", 1, 1.0, Instant.now())))
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  void delete_whenExists_shouldDelete() {
    UUID id = UUID.randomUUID();
    when(bookRepository.existsById(id)).thenReturn(true);

    bookService.delete(id);

    verify(bookRepository).deleteById(id);
  }

  @Test
  void delete_whenMissing_shouldThrow() {
    UUID id = UUID.randomUUID();
    when(bookRepository.existsById(id)).thenReturn(false);

    assertThatThrownBy(() -> bookService.delete(id)).isInstanceOf(EntityNotFoundException.class);

    verify(bookRepository, never()).deleteById(any());
  }
}
