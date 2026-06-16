package com.school.haja.service;

import com.school.haja.entities.BookEdition;
import com.school.haja.repository.BookEditionRepository;
import com.school.haja.repository.model.JBookEdition;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Couche service pour l'entité {@link BookEdition}. */
@Service
@AllArgsConstructor
@Transactional
public class BookEditionService {

  private final BookEditionRepository bookEditionRepository;

  public BookEdition create(BookEdition bookEdition) {
    JBookEdition saved = bookEditionRepository.save(toEntity(bookEdition));
    return toDomain(saved);
  }

  public BookEdition getById(UUID id) {
    return bookEditionRepository
        .findById(id)
        .map(this::toDomain)
        .orElseThrow(() -> new EntityNotFoundException("BookEdition not found: " + id));
  }

  public List<BookEdition> getAll() {
    return bookEditionRepository.findAll().stream()
        .map(this::toDomain)
        .collect(Collectors.toList());
  }

  public BookEdition update(UUID id, BookEdition bookEdition) {
    JBookEdition existing =
        bookEditionRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("BookEdition not found: " + id));

    existing.setType(bookEdition.getType());

    return toDomain(bookEditionRepository.save(existing));
  }

  public void delete(UUID id) {
    if (!bookEditionRepository.existsById(id)) {
      throw new EntityNotFoundException("BookEdition not found: " + id);
    }
    bookEditionRepository.deleteById(id);
  }

  private JBookEdition toEntity(BookEdition bookEdition) {
    return new JBookEdition(bookEdition.getId(), bookEdition.getType());
  }

  private BookEdition toDomain(JBookEdition entity) {
    return new BookEdition(entity.getId(), entity.getType());
  }
}
