package com.school.haja.service;

import com.school.haja.entities.Book;
import com.school.haja.repository.BookRepository;
import com.school.haja.repository.model.JBook;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Couche service pour l'entité {@link Book}. Contient la logique métier et fait le pont entre les
 * contrôleurs et le {@link BookRepository} (couche persistance).
 */
@Service
@AllArgsConstructor
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    public Book create(Book book) {
        JBook saved = bookRepository.save(toEntity(book));
        return toDomain(saved);
    }

    public Book getById(UUID id) {
        return bookRepository
                .findById(id)
                .map(this::toDomain)
                .orElseThrow(() -> new EntityNotFoundException("Book not found: " + id));
    }

    public List<Book> getAll() {
        return bookRepository.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    public Book update(UUID id, Book book) {
        JBook existing =
                bookRepository
                        .findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Book not found: " + id));

        existing.setTitle(book.getTitle());
        existing.setIsbn(book.getIsbn());
        existing.setNbr_page(book.getNbr_page());
        existing.setPrice(book.getPrice());
        existing.setRelease_date(book.getRelease_date());

        return toDomain(bookRepository.save(existing));
    }

    public void delete(UUID id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book not found: " + id);
        }
        bookRepository.deleteById(id);
    }

    private JBook toEntity(Book book) {
        return new JBook(
                book.getId(),
                book.getTitle(),
                book.getIsbn(),
                book.getNbr_page(),
                book.getPrice(),
                book.getRelease_date());
    }

    private Book toDomain(JBook entity) {
        return new Book(
                entity.getId(),
                entity.getTitle(),
                entity.getIsbn(),
                entity.getNbr_page(),
                entity.getPrice(),
                entity.getRelease_date());
    }
}