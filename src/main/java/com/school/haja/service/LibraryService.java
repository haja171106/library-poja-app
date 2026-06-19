package com.school.haja.service;

import com.school.haja.entities.Library;
import com.school.haja.repository.LibraryRepository;
import com.school.haja.repository.model.JLibrary;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Couche service pour l'entité {@link Library}. */
@Service
@AllArgsConstructor
@Transactional
public class LibraryService {

  private final LibraryRepository libraryRepository;

  public Library create(Library library) {
    JLibrary saved = libraryRepository.save(toEntity(library));
    return toDomain(saved);
  }

  public Library getById(UUID id) {
    return libraryRepository
        .findById(id)
        .map(this::toDomain)
        .orElseThrow(() -> new EntityNotFoundException("Library not found: " + id));
  }

  public List<Library> getAll() {
    return libraryRepository.findAll().stream().map(this::toDomain).collect(Collectors.toList());
  }

  public Library update(UUID id, Library library) {
    JLibrary existing =
        libraryRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Library not found: " + id));

    existing.setName(library.getName());

    return toDomain(libraryRepository.save(existing));
  }

  public void delete(UUID id) {
    if (!libraryRepository.existsById(id)) {
      throw new EntityNotFoundException("Library not found: " + id);
    }
    libraryRepository.deleteById(id);
  }

  private JLibrary toEntity(Library library) {
    JLibrary entity = new JLibrary();
    entity.setId(library.getId());
    entity.setName(library.getName());
    return entity;
  }

  private Library toDomain(JLibrary entity) {
    return new Library(entity.getId(), entity.getName());
  }
}
