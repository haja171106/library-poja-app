package com.school.haja.repository;

import com.school.haja.repository.model.JBook;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<JBook, UUID> {

  @Override
  List<JBook> findAll();

  Optional<JBook> findByIsbn(String isbn);

  List<JBook> findByTitleContainingIgnoreCase(String title);

  boolean existsByIsbn(String isbn);
}
