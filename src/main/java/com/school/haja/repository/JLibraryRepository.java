package com.school.haja.repository;

import com.school.haja.repository.model.JLibrary;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JLibraryRepository extends JpaRepository<JLibrary, UUID> {

  @Override
  List<JLibrary> findAll();

  Optional<JLibrary> findByName(String name);

  boolean existsByName(String name);
}
