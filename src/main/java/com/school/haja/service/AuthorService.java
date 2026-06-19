package com.school.haja.service;

import com.school.haja.entities.Author;
import com.school.haja.repository.AuthorRepository;
import com.school.haja.repository.model.JAuthor;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Couche service pour l'entité {@link Author}. */
@Service
@AllArgsConstructor
@Transactional
public class AuthorService {

  private final AuthorRepository authorRepository;

  public Author create(Author author) {
    JAuthor saved = authorRepository.save(toEntity(author));
    return toDomain(saved);
  }

  public Author getById(UUID id) {
    return authorRepository
        .findById(id)
        .map(this::toDomain)
        .orElseThrow(() -> new EntityNotFoundException("Author not found: " + id));
  }

  public List<Author> getAll() {
    return authorRepository.findAll().stream().map(this::toDomain).collect(Collectors.toList());
  }

  public Author update(UUID id, Author author) {
    JAuthor existing =
        authorRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Author not found: " + id));

    existing.setFirstname(author.getFirstname());
    existing.setLastname(author.getLastname());
    existing.setBirthday(author.getBirthday());
    existing.setSex(author.getSex());

    return toDomain(authorRepository.save(existing));
  }

  public void delete(UUID id) {
    if (!authorRepository.existsById(id)) {
      throw new EntityNotFoundException("Author not found: " + id);
    }
    authorRepository.deleteById(id);
  }

  private JAuthor toEntity(Author author) {
    return new JAuthor(
        author.getId(),
        author.getFirstname(),
        author.getLastname(),
        author.getBirthday(),
        author.getSex());
  }

  private Author toDomain(JAuthor entity) {
    return new Author(
        entity.getId(),
        entity.getFirstname(),
        entity.getLastname(),
        entity.getBirthday(),
        entity.getSex());
  }
}
