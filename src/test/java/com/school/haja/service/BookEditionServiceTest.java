package com.school.haja.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.school.haja.entities.BookEdition;
import com.school.haja.repository.BookEditionRepository;
import com.school.haja.repository.model.JBookEdition;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookEditionServiceTest {

  @Mock private BookEditionRepository bookEditionRepository;

  @InjectMocks private BookEditionService bookEditionService;

  private JBookEdition jBookEdition(UUID id, String type) {
    JBookEdition entity = new JBookEdition();
    entity.setId(id);
    entity.setType(type);
    return entity;
  }

  @Test
  void create_shouldSaveAndReturnBookEdition() {
    UUID id = UUID.randomUUID();
    when(bookEditionRepository.save(any(JBookEdition.class)))
        .thenReturn(jBookEdition(id, "Grand Format"));

    BookEdition result = bookEditionService.create(new BookEdition(id, "Grand Format"));

    assertThat(result.getId()).isEqualTo(id);
    assertThat(result.getType()).isEqualTo("Grand Format");
  }

  @Test
  void getById_whenExists_shouldReturnBookEdition() {
    UUID id = UUID.randomUUID();
    when(bookEditionRepository.findById(id)).thenReturn(Optional.of(jBookEdition(id, "Poche")));

    BookEdition result = bookEditionService.getById(id);

    assertThat(result.getType()).isEqualTo("Poche");
  }

  @Test
  void getById_whenMissing_shouldThrow() {
    UUID id = UUID.randomUUID();
    when(bookEditionRepository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> bookEditionService.getById(id))
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  void getAll_shouldReturnAllBookEditions() {
    when(bookEditionRepository.findAll())
        .thenReturn(
            List.of(jBookEdition(UUID.randomUUID(), "A"), jBookEdition(UUID.randomUUID(), "B")));

    List<BookEdition> result = bookEditionService.getAll();

    assertThat(result).hasSize(2);
  }

  @Test
  void update_whenExists_shouldUpdateAndReturnBookEdition() {
    UUID id = UUID.randomUUID();
    JBookEdition existing = jBookEdition(id, "OldType");
    when(bookEditionRepository.findById(id)).thenReturn(Optional.of(existing));
    when(bookEditionRepository.save(existing)).thenReturn(existing);

    BookEdition result = bookEditionService.update(id, new BookEdition(id, "NewType"));

    assertThat(result.getType()).isEqualTo("NewType");
  }

  @Test
  void update_whenMissing_shouldThrow() {
    UUID id = UUID.randomUUID();
    when(bookEditionRepository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> bookEditionService.update(id, new BookEdition(id, "X")))
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  void delete_whenExists_shouldDelete() {
    UUID id = UUID.randomUUID();
    when(bookEditionRepository.existsById(id)).thenReturn(true);

    bookEditionService.delete(id);

    verify(bookEditionRepository).deleteById(id);
  }

  @Test
  void delete_whenMissing_shouldThrow() {
    UUID id = UUID.randomUUID();
    when(bookEditionRepository.existsById(id)).thenReturn(false);

    assertThatThrownBy(() -> bookEditionService.delete(id))
        .isInstanceOf(EntityNotFoundException.class);

    verify(bookEditionRepository, never()).deleteById(any());
  }
}
