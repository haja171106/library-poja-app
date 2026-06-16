package com.school.haja.repository;

import com.school.haja.repository.model.JGenre;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<JGenre, UUID> {

  @Override
  List<JGenre> findAll();

  Optional<JGenre> findByType(String type);

  boolean existsByType(String type);
}
