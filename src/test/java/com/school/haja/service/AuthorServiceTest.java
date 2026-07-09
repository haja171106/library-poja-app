package com.school.haja.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.school.haja.entities.Author;
import com.school.haja.repository.AuthorRepository;
import com.school.haja.repository.model.JAuthor;
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
class AuthorServiceTest {

  @Mock private AuthorRepository authorRepository;

  @InjectMocks private AuthorService authorService;

  @Test
  void create_shouldSaveAndReturnAuthor() {
    UUID id = UUID.randomUUID();
    Instant birthday = Instant.parse("1980-01-01T00:00:00Z");
    Author author = new Author(id, "Victor", "Hugo", birthday, "M");
    JAuthor saved = new JAuthor(id, "Victor", "Hugo", birthday, "M");

    when(authorRepository.save(any(JAuthor.class))).thenReturn(saved);

    Author result = authorService.create(author);

    assertThat(result.getId()).isEqualTo(id);
    assertThat(result.getFirstname()).isEqualTo("Victor");
    assertThat(result.getLastname()).isEqualTo("Hugo");
    assertThat(result.getBirthday()).isEqualTo(birthday);
    assertThat(result.getSex()).isEqualTo("M");
  }

  @Test
  void getById_whenExists_shouldReturnAuthor() {
    UUID id = UUID.randomUUID();
    JAuthor entity = new JAuthor(id, "Jane", "Austen", Instant.now(), "F");
    when(authorRepository.findById(id)).thenReturn(Optional.of(entity));

    Author result = authorService.getById(id);

    assertThat(result.getFirstname()).isEqualTo("Jane");
  }

  @Test
  void getById_whenMissing_shouldThrow() {
    UUID id = UUID.randomUUID();
    when(authorRepository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> authorService.getById(id))
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  void getAll_shouldReturnAllAuthors() {
    JAuthor a1 = new JAuthor(UUID.randomUUID(), "A", "B", Instant.now(), "M");
    JAuthor a2 = new JAuthor(UUID.randomUUID(), "C", "D", Instant.now(), "F");
    when(authorRepository.findAll()).thenReturn(List.of(a1, a2));

    List<Author> result = authorService.getAll();

    assertThat(result).hasSize(2);
  }

  @Test
  void update_whenExists_shouldUpdateAndReturnAuthor() {
    UUID id = UUID.randomUUID();
    Instant newBirthday = Instant.parse("1990-05-05T00:00:00Z");
    JAuthor existing = new JAuthor(id, "Old", "Name", Instant.now(), "M");
    Author update = new Author(id, "New", "Name2", newBirthday, "F");

    when(authorRepository.findById(id)).thenReturn(Optional.of(existing));
    when(authorRepository.save(existing)).thenReturn(existing);

    Author result = authorService.update(id, update);

    assertThat(result.getFirstname()).isEqualTo("New");
    assertThat(result.getLastname()).isEqualTo("Name2");
    assertThat(result.getBirthday()).isEqualTo(newBirthday);
    assertThat(result.getSex()).isEqualTo("F");
  }

  @Test
  void update_whenMissing_shouldThrow() {
    UUID id = UUID.randomUUID();
    when(authorRepository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> authorService.update(id, new Author(id, "X", "Y", Instant.now(), "M")))
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  void delete_whenExists_shouldDelete() {
    UUID id = UUID.randomUUID();
    when(authorRepository.existsById(id)).thenReturn(true);

    authorService.delete(id);

    verify(authorRepository).deleteById(id);
  }

  @Test
  void delete_whenMissing_shouldThrow() {
    UUID id = UUID.randomUUID();
    when(authorRepository.existsById(id)).thenReturn(false);

    assertThatThrownBy(() -> authorService.delete(id))
        .isInstanceOf(EntityNotFoundException.class);

    verify(authorRepository, never()).deleteById(any());
  }
}
