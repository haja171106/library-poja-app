package com.school.haja.service;

import com.school.haja.entities.Genre;
import com.school.haja.repository.GenreRepository;
import com.school.haja.repository.model.JGenre;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Couche service pour l'entité {@link Genre}. */
@Service
@AllArgsConstructor
@Transactional
public class GenreService {

  private final GenreRepository genreRepository;

  public Genre create(Genre genre) {
    JGenre saved = genreRepository.save(toEntity(genre));
    return toDomain(saved);
  }

  public Genre getById(UUID id) {
    return genreRepository
        .findById(id)
        .map(this::toDomain)
        .orElseThrow(() -> new EntityNotFoundException("Genre not found: " + id));
  }

  public List<Genre> getAll() {
    return genreRepository.findAll().stream().map(this::toDomain).collect(Collectors.toList());
  }

  public Genre update(UUID id, Genre genre) {
    JGenre existing =
        genreRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Genre not found: " + id));

    existing.setType(genre.getType());

    return toDomain(genreRepository.save(existing));
  }

  public void delete(UUID id) {
    if (!genreRepository.existsById(id)) {
      throw new EntityNotFoundException("Genre not found: " + id);
    }
    genreRepository.deleteById(id);
  }

  private JGenre toEntity(Genre genre) {
    return new JGenre(genre.getId(), genre.getType());
  }

  private Genre toDomain(JGenre entity) {
    return new Genre(entity.getId(), entity.getType());
  }
}
