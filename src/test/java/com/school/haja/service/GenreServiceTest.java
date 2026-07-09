package com.school.haja.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.school.haja.entities.Genre;
import com.school.haja.repository.GenreRepository;
import com.school.haja.repository.model.JGenre;
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
class GenreServiceTest {

  @Mock private GenreRepository genreRepository;

  @InjectMocks private GenreService genreService;

  @Test
  void create_shouldSaveAndReturnGenre() {
    UUID id = UUID.randomUUID();
    Genre genre = new Genre(id, "Fantasy");
    JGenre saved = new JGenre(id, "Fantasy");

    when(genreRepository.save(any(JGenre.class))).thenReturn(saved);

    Genre result = genreService.create(genre);

    assertThat(result.getId()).isEqualTo(id);
    assertThat(result.getType()).isEqualTo("Fantasy");
  }

  @Test
  void getById_whenExists_shouldReturnGenre() {
    UUID id = UUID.randomUUID();
    JGenre entity = new JGenre(id, "Horror");

    when(genreRepository.findById(id)).thenReturn(Optional.of(entity));

    Genre result = genreService.getById(id);

    assertThat(result.getId()).isEqualTo(id);
    assertThat(result.getType()).isEqualTo("Horror");
  }

  @Test
  void getById_whenMissing_shouldThrow() {
    UUID id = UUID.randomUUID();
    when(genreRepository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> genreService.getById(id))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining(id.toString());
  }

  @Test
  void getAll_shouldReturnAllGenres() {
    JGenre g1 = new JGenre(UUID.randomUUID(), "Drama");
    JGenre g2 = new JGenre(UUID.randomUUID(), "Comedy");
    when(genreRepository.findAll()).thenReturn(List.of(g1, g2));

    List<Genre> result = genreService.getAll();

    assertThat(result).hasSize(2);
    assertThat(result).extracting(Genre::getType).containsExactlyInAnyOrder("Drama", "Comedy");
  }

  @Test
  void update_whenExists_shouldUpdateAndReturnGenre() {
    UUID id = UUID.randomUUID();
    JGenre existing = new JGenre(id, "OldType");
    Genre update = new Genre(id, "NewType");

    when(genreRepository.findById(id)).thenReturn(Optional.of(existing));
    when(genreRepository.save(existing)).thenReturn(existing);

    Genre result = genreService.update(id, update);

    assertThat(result.getType()).isEqualTo("NewType");
    verify(genreRepository).save(existing);
  }

  @Test
  void update_whenMissing_shouldThrow() {
    UUID id = UUID.randomUUID();
    when(genreRepository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> genreService.update(id, new Genre(id, "X")))
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  void delete_whenExists_shouldDelete() {
    UUID id = UUID.randomUUID();
    when(genreRepository.existsById(id)).thenReturn(true);

    genreService.delete(id);

    verify(genreRepository, times(1)).deleteById(id);
  }

  @Test
  void delete_whenMissing_shouldThrow() {
    UUID id = UUID.randomUUID();
    when(genreRepository.existsById(id)).thenReturn(false);

    assertThatThrownBy(() -> genreService.delete(id)).isInstanceOf(EntityNotFoundException.class);

    verify(genreRepository, never()).deleteById(any());
  }
}
